package tk.kdev.model;

public class Rate {
	private String no;
	private String effectiveDate;
	private String bid;
	private String ask;
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getAsk() {
		return ask;
	}
	public void setAsk(String ask) {
		this.ask = ask;
	}
	@Override
	public String toString() {
		return "Rate [no=" + no + ", effectiveDate=" + effectiveDate + ", bid=" + bid + ", ask=" + ask + "]";
	}
	
	
}
