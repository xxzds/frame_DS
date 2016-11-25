package com.anjz.core.model.wechat.qrcode;

/**
 * 临时二维码：{"expire_seconds": 604800, "action_name": "QR_SCENE", "action_info": {"scene": {"scene_id": 123}}}
 * 永久二维码：
 * 1.{"action_name": "QR_LIMIT_SCENE", "action_info": {"scene": {"scene_id": 123}}}
 * 2.{"action_name": "QR_LIMIT_STR_SCENE", "action_info": {"scene": {"scene_str": "123"}}}
 * 
 * 
 * 创建二维码ticket的实体
 * @author ding.shuai
 * @date 2016年9月29日上午11:11:35
 */
public class QrcodeCreateEntity {

	/**
	 * 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
	 */
	private Long expire_seconds;
	
	/**
	 * 二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久,QR_LIMIT_STR_SCENE为永久的字符串参数值
	 */
	private ActionName action_name;
	
	/**
	 * 二维码详细信息
	 */
	private ActionInfo action_info;
	
	public Long getExpire_seconds() {
		return expire_seconds;
	}

	public void setExpire_seconds(Long expire_seconds) {
		this.expire_seconds = expire_seconds;
	}

	public ActionName getAction_name() {
		return action_name;
	}

	public void setAction_name(ActionName action_name) {
		this.action_name = action_name;
	}

	public ActionInfo getAction_info() {
		return action_info;
	}

	public void setAction_info(ActionInfo action_info) {
		this.action_info = action_info;
	}

	/**
	 * 内部类
	 * @author ding.shuai
	 * @date 2016年9月29日上午11:21:21
	 */
	public static class ActionInfo{
		private Scene scene;

		public Scene getScene() {
			return scene;
		}

		public void setScene(Scene scene) {
			this.scene = scene;
		}		
	}
	
	/**
	 * 内部类
	 * @author ding.shuai
	 * @date 2016年9月29日上午11:21:34
	 */
	public static class Scene{
		
		/**
		 * 场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
		 */
		private Long scene_id;
		
		/**
		 * 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64，仅永久二维码支持此字段
		 */
		private String scene_str;

		public String getScene_str() {
			return scene_str;
		}

		public void setScene_str(String scene_str) {
			this.scene_str = scene_str;
		}

		public Long getScene_id() {
			return scene_id;
		}

		public void setScene_id(Long scene_id) {
			this.scene_id = scene_id;
		}
	}
	
	/**
	 * 枚举类
	 * @author ding.shuai
	 * @date 2016年9月29日上午11:21:41
	 */
	public static enum ActionName{
		QR_SCENE("临时"),
		QR_LIMIT_SCENE("永久"),
		QR_LIMIT_STR_SCENE("永久的字符串参数值");
		
		private String info;		
		ActionName(String info){
			this.info=info;
		}

		public String getInfo() {
			return info;
		}
		public void setInfo(String info) {
			this.info = info;
		}
	}
}
