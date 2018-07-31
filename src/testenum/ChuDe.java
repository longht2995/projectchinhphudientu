package testenum;

public enum ChuDe {
	CAT_ID_GIOITHIEU("Giới thiệu"),
	CAT_ID_TINTUC("Tin tức"),
	CAT_ID_TUONGTACCUTRI("Tương tác với cử tri"),
	CAT_ID_HOATDONG("Hoạt động chỉ đạo điều hành"),
	CAT_ID_DAIBIEU("Đại biểu HĐND"),
	CAT_ID_LIENHE("Liên hệ");

	private String tenChuDe;

	private ChuDe(final String name) {
		this.tenChuDe = name;
	}
	
	public String getTenChuDe() {
		return tenChuDe;
	}

	public void setTenChuDe(String tenChuDe) {
		this.tenChuDe = tenChuDe;
	}
}