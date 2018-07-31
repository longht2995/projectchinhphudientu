package vn.greenglobal.front.service;

import java.util.ArrayList;
import java.util.List;

import vn.toancauxanh.gg.model.DanhMuc;
import vn.toancauxanh.gg.model.QTinBai;
import vn.toancauxanh.gg.model.TinBai;
import vn.toancauxanh.service.BasicService;

public class TinBaiService extends BasicService<TinBai> {

	public List<TinBai> getTop5HdndHuyen() {
		DanhMuc objDanhMuc = em().find(DanhMuc.class, 7l);
		List<TinBai> list = new ArrayList<TinBai>();
		list = find(TinBai.class).where(QTinBai.tinBai.trangThai.ne(core().TT_DA_XOA))
				.where(QTinBai.tinBai.categories.contains(objDanhMuc)).limit(5).orderBy(QTinBai.tinBai.ngaySua.desc())
				.fetch();
		return list;
	}
}
