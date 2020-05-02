package jp.co.internous.smile.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.smile.model.domain.TblCart;
import jp.co.internous.smile.model.domain.dto.CartDto;
import jp.co.internous.smile.model.form.CartForm;
import jp.co.internous.smile.model.mapper.TblCartMapper;
import jp.co.internous.smile.model.session.LoginSession;

@Controller
@RequestMapping("/smile/cart")
public class CartController {
	
	@Autowired
	private LoginSession loginSession;		//ログイン状態をページ間で保持する。

	@Autowired
	private TblCartMapper cartMapper;

	private Gson gson = new Gson();		
	
	@RequestMapping("/")				
	public String index(Model m) {
		
		int userId = loginSession.getLogined() ? loginSession.getUserId() : loginSession.getTmpUserId();
		
		List<CartDto> carts = cartMapper.findByUserId(userId);		//Listコレクションの使用。配列とは異なり可変(後で追加、削除ができる。)
		
		//addAttributeでオブジェクトにデータの値を格納する。第一引数がテンプレートから参照する変数名、第二引数がオブジェクト名。
		//これにより、HTML側から${変数名}で呼び出せる。
		
		m.addAttribute("loginSession",loginSession);				
		m.addAttribute("carts",carts);
		
		return "cart";
	}
	
	@RequestMapping("/add")
	public String addCart(CartForm f, Model m) {
		
		int userId = loginSession.getLogined() ? loginSession.getUserId() : loginSession.getTmpUserId();
		
		f.setUserId(userId);
		
		TblCart cart = new TblCart(f);		//コンストラクタ呼び出し。引数の型はCartFormクラス型
		int result = 0;
		if (cartMapper.findCountByUserIdAndProductId(userId, f.getProductId()) > 0) {
			result = cartMapper.updated(cart);
		} else {
			result = cartMapper.insert(cart);
		}
		if (result > 0) {
			List<CartDto> carts = cartMapper.findByUserId(userId);
			m.addAttribute("loginSession", loginSession);
			m.addAttribute("carts", carts);
		}
		return "cart";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/delete")
	@ResponseBody
	public boolean deleteCart(@RequestBody String checkedIdList) {
		
		int result = 0;
		
		Map<String, List<String>> map = gson.fromJson(checkedIdList, Map.class);
		List<String> checkedIds = map.get("checkedIdList");
		for (String id : checkedIds) {
			result += cartMapper.deleteById(Integer.parseInt(id));
		}
		
		return result > 0;
	}
	
}