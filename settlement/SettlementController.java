package jp.co.internous.smile.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.smile.model.domain.MstDestination;
import jp.co.internous.smile.model.mapper.MstDestinationMapper;
import jp.co.internous.smile.model.mapper.TblCartMapper;
import jp.co.internous.smile.model.mapper.TblPurchaseHistoryMapper;
import jp.co.internous.smile.model.session.LoginSession;

@Controller
@RequestMapping("/smile/settlement")

public class SettlementController {
	
	@Autowired
	LoginSession loginSession;
	
	@Autowired
	TblCartMapper cartMapper;
	
	@Autowired
	MstDestinationMapper destinationMapper;
	
	@Autowired
	TblPurchaseHistoryMapper purchaseHistoryMapper;
	
	private Gson gson = new Gson();
	
	@RequestMapping("/")
	public String index(Model m) {
		
		int userId = loginSession.getUserId();
		
		//useridによって宛先情報を取得するメソッド		
		List<MstDestination> destinations = destinationMapper.findByUserId(userId); 
		m.addAttribute("loginSession", loginSession);
		m.addAttribute("settlements", destinations);
		
		return "settlement";
	}
	
	@SuppressWarnings("unchecked")	//メソッドの戻り値が格納先の変数の型の値として得られているかチェックする。動作そのものには関係なくビルド時に警告が出ないようにするもの
	@RequestMapping("/complete")
	@ResponseBody
	public boolean complete(@RequestBody String destinationId) {
		
		Map<String, String> map = gson.fromJson(destinationId, Map.class);
		String id = map.get("destinationId");
		
		int userId = loginSession.getUserId();
		
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("destinationId", id);
		parameter.put("userId", userId);
		
		//TblPurchaseHistoryMapper.xmlのinsertメソッド	
		int insertCount = purchaseHistoryMapper.insert(parameter);
		
		int deleteCount = 0;
		
		if (insertCount > 0) {
			deleteCount = cartMapper.deleteByUserId(userId);
		}
		return deleteCount == insertCount;
	}
	
}