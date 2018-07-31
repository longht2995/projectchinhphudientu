package vn.toancauxanh.cms.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.DanhMuc;
import vn.toancauxanh.gg.model.QDanhMuc;
import vn.toancauxanh.gg.model.QTinBai;
import vn.toancauxanh.gg.model.TinBai;
import vn.toancauxanh.service.BasicService;

public class TinBaiService extends BasicService<TinBai> {

	public JPAQuery<TinBai> getTargetQuery() {
		String paramTuKhoa = MapUtils.getString(argDeco(), Labels.getLabel("param.tukhoa"), "").trim();
		long tacgia_id = MapUtils.getLongValue(argDeco(), Labels.getLabel("param.tacgia"));
		String trangThaiSoan = MapUtils.getString(argDeco(), Labels.getLabel("param.trangthaisoan"), "").trim();
		long chuDe = MapUtils.getLongValue(argDeco(), Labels.getLabel("param.category"));
		String paramTrangThaiNoiBat = MapUtils.getString(argDeco(), Labels.getLabel("param.trangthainoibat"), "");
		String madinhdanh = MapUtils.getString(argDeco(), Labels.getLabel("param.madinhdanh"), "").trim();

		JPAQuery<TinBai> q = find(TinBai.class).where(QTinBai.tinBai.trangThai.ne(core().TT_DA_XOA));

		if (paramTuKhoa != null && !paramTuKhoa.isEmpty()) {
			String tukhoa = "%" + paramTuKhoa + "%";
			q.where(QTinBai.tinBai.title.like(tukhoa).or(QTinBai.tinBai.description.like(tukhoa)));
		}

		if (tacgia_id > 0) {
			q.where(QTinBai.tinBai.author.id.eq(tacgia_id));
		}
		if (trangThaiSoan != null && !trangThaiSoan.isEmpty()) {
			q.where(QTinBai.tinBai.trangThaiSoan.eq(trangThaiSoan));
		}
		if (chuDe > 0) {
			DanhMuc category = em().find(DanhMuc.class, chuDe);
			List<DanhMuc> children = find(DanhMuc.class).where(QDanhMuc.danhMuc.parent.eq(category))
					.where(QDanhMuc.danhMuc.trangThai.eq(core().TT_AP_DUNG)).fetch();
			q.where(QTinBai.tinBai.categories.any().in(children).or(QTinBai.tinBai.categories.contains(category)));
		}

		if ("true".equals(paramTrangThaiNoiBat)) {
			q.where(QTinBai.tinBai.noiBat.eq(true));
		} else if ("false".equals(paramTrangThaiNoiBat)) {
			q.where(QTinBai.tinBai.noiBat.eq(false));
		}

		if (getFixTuNgay() != null && getFixDenNgay() == null) {
			q.where(QTinBai.tinBai.dateBeginTime.after(getFixTuNgay()));
		} else if (getFixTuNgay() == null && getFixDenNgay() != null) {
			q.where(QTinBai.tinBai.dateBeginTime.before(getFixDenNgay()));
		} else if (getFixTuNgay() != null && getFixDenNgay() != null) {
			q.where(QTinBai.tinBai.dateBeginTime.between(getFixTuNgay(), getFixDenNgay()));
		}
		
		if (madinhdanh != null && !madinhdanh.isEmpty()) {
			String ma = "%" + madinhdanh + "%";
			q.where(QTinBai.tinBai.maDinhDanh.like(ma));
		}
		return q.orderBy(QTinBai.tinBai.ngayTao.desc());
	}

	public List<TinBai> getListTinBaiAndNull() {
		JPAQuery<TinBai> q = find(TinBai.class);
		List<TinBai> list = new ArrayList<TinBai>();
		list.add(null);
		for (TinBai tin : q.where(QTinBai.tinBai.trangThai.eq(core().TT_AP_DUNG)).fetch()) {
			list.add(tin);
		}
		return list;
	}
}
