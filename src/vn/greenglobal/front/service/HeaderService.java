package vn.greenglobal.front.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;

import testenum.ChuDe;
import vn.toancauxanh.gg.model.DanhMuc;
import vn.toancauxanh.gg.model.ThamSo;
import vn.toancauxanh.gg.model.enums.ThamSoEnum;
import vn.toancauxanh.model.NhanVien;

public class HeaderService extends FrontService {

	private DanhMuc gioiThieu;
	private DanhMuc tinTuc;
	private DanhMuc lienHe;
	private NhanVien user;
	
	public List<String> getTitle(){
		List<String> list = new ArrayList<String>();
		for(ChuDe string: ChuDe.values()){
			list.add(string.getTenChuDe());
		}
		return list;
	}

	public NhanVien getUser() {
		if (user == null) {
			user = getNhanVien();
		}
		return user;
	}

	public DanhMuc getGioiThieu() {
		if (gioiThieu == null) {
			ThamSo thamSo = core().getThamSos().getThamSoByKey(ThamSoEnum.CAT_ID_GIOITHIEU).fetchOne();
			String strId = thamSo != null ? thamSo.getValue() : "0";
			long id = Long.parseLong(strId);
			gioiThieu = core().getDanhMuc().getById(id).fetchFirst();
		}
		return gioiThieu;
	}

	public DanhMuc getTinTuc() {
		if (tinTuc == null) {
			ThamSo thamSo = core().getThamSos().getThamSoByKey(ThamSoEnum.CAT_ID_TINTUC).fetchOne();
			String strId = thamSo != null ? thamSo.getValue() : "0";
			long id = Long.parseLong(strId);
			tinTuc = core().getDanhMuc().getById(id).fetchFirst();
		}
		return tinTuc;
	}

	public DanhMuc getLienHe() {
		if (lienHe == null) {
			ThamSo thamSo = core().getThamSos().getThamSoByKey(ThamSoEnum.CAT_ID_LIENHE).fetchOne();
			String strId = thamSo != null ? thamSo.getValue() : "0";
			long id = Long.parseLong(strId);
			lienHe = core().getDanhMuc().getById(id).fetchFirst();
		}
		return lienHe;
	}

	@Command
	public void search() {
		String tuKhoa = MapUtils.getString(argDeco(), "tukhoa");
		if (tuKhoa != null && !tuKhoa.isEmpty()) {
			String param = "";
			param = ("".equals(param) ? "" : param + "&") + (tuKhoa != null ? "tukhoa=" + tuKhoa.trim() : "");
			Executions.sendRedirect("/timkiem?" + param);
		}
	}

	public boolean isOpen(String resource, String cat) {
		if (cat.equals("tintuc")) {
			for (DanhMuc each : tinTuc.getChild()) {
				if (each.getAlias().equals(resource)) {
					return true;
				}
			}
		}

		if (cat.equals("gioithieu")) {
			for (DanhMuc each : gioiThieu.getChild()) {
				if (each.getAlias().equals(resource)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void redirectLogin(HttpServletRequest req, HttpServletResponse res) {
		// K redirect
	}
}
