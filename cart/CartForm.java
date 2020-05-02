//画面サイドからjavaオブジェクトへ渡すデータを管理するクラス
//MapperクラスのListで表示される

package jp.co.internous.smile.model.form;

import java.io.Serializable;

public class CartForm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int userId;
	private int productId;
	private int productCount;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getProductCount() {
		return productCount;
	}
	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}
	
}