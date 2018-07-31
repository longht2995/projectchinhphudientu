package vn.toancauxanh.cms.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.QVanBan;
import vn.toancauxanh.gg.model.VanBan;
import vn.toancauxanh.service.BasicService;

public class VanBanService extends BasicService<VanBan>{
	long total;
	public JPAQuery<VanBan> getTargetQuery(){
		String tukhoa = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		Long loaivanban = (Long) argDeco().get(Labels.getLabel("param.loaivanban"));
		Long linhvuc = (Long) argDeco().get(Labels.getLabel("param.linhvuc"));
		Long coquanbanhanh = (Long) argDeco().get(Labels.getLabel("param.coquanbanhanh"));
		Long capbanhanh = (Long) argDeco().get(Labels.getLabel("param.capbanhanh"));
		String trangthaixuatban = MapUtils.getString(argDeco(),Labels.getLabel("param.trangthai"),"").trim();
		Date from = (Date) argDeco().get(Labels.getLabel("param.tungay"));
		Date to = (Date) argDeco().get(Labels.getLabel("param.denngay"));
		
		JPAQuery<VanBan> q = find(VanBan.class)
				.where(QVanBan.vanBan.trangThai.ne(core().TT_DA_XOA));
		if(tukhoa!=null && !tukhoa.isEmpty()) {
			q.where(QVanBan.vanBan.soKyHieu.like("%"+tukhoa+"%")
				.or(QVanBan.vanBan.trichYeu.like("%"+tukhoa+"%")));
		}
		if(loaivanban!=null) {
			q.where(QVanBan.vanBan.loaiVanBan.id.eq(loaivanban));
		}
		if(linhvuc!=null) {
			q.where(QVanBan.vanBan.linhVucVanBan.id.eq(linhvuc));
		}
		if(coquanbanhanh!=null) {
			q.where(QVanBan.vanBan.coQuanBanHanh.id.eq(coquanbanhanh));
		}
		if(capbanhanh!=null) {
			q.where(QVanBan.vanBan.capBanHanh.id.eq(capbanhanh));
		}
		if(trangthaixuatban!=null && !trangthaixuatban.isEmpty()) {
			System.out.println(trangthaixuatban);
			q.where(QVanBan.vanBan.trangThai.eq(trangthaixuatban));
		}
		if(from!=null) {
			q.where(QVanBan.vanBan.ngayBanHanh.goe(from));
		}
		if(to!=null) {
			q.where(QVanBan.vanBan.ngayBanHanh.loe(to));
		}
		
		q.orderBy(QVanBan.vanBan.ngaySua.desc());
		return q;
	}
	public JPAQuery<VanBan> getTargetQueryFont(){
		String sokyhieu = MapUtils.getString(argDeco(), Labels.getLabel("param.sokyhieu"));
		String trichyeu = MapUtils.getString(argDeco(), Labels.getLabel("param.trichyeu"));
		String coquanbanhanh = MapUtils.getString(argDeco(), Labels.getLabel("param.coquanbanhanh"));
		JPAQuery<VanBan> q = find(VanBan.class)
				.where(QVanBan.vanBan.trangThai.ne(core().TT_DA_XOA));
		if(sokyhieu!=null && !sokyhieu.isEmpty()) {
			q.where(QVanBan.vanBan.soKyHieu.like("%"+sokyhieu+"%"));
		}
		if(trichyeu!=null && !trichyeu.isEmpty()) {
			q.where(QVanBan.vanBan.trichYeu.like("%"+trichyeu+"%"));
		}
		if(coquanbanhanh!=null) {
			try {
				q.where(QVanBan.vanBan.coQuanBanHanh.id.eq(Long.parseLong(coquanbanhanh)));
			}catch(Exception ex) {
				
			}
		}
		getArg().put("total", getSizeTargetQuery(q));
		q.orderBy(QVanBan.vanBan.ngaySua.desc());
		return q;
	}
	public long getSizeTargetQuery(JPAQuery<VanBan> q) {
		return q.fetchCount();
	}
	@Command
	public void pageVanBan(@BindingParam("skh")String skh,@BindingParam("ty")String ty,@BindingParam("cbx")String cbx) throws IOException {
		String param = "";
		param += ("".equals(param)?"":"&") + (cbx!=null&&!cbx.isEmpty()?"coquanbanhanh="+cbx.trim():"");
		param += ("".equals(param)?"":"&") + (skh!=null&&!skh.isEmpty()?"sokyhieu="+skh.trim():"");
		param += ("".equals(param)?"":"&") + (ty!=null&&!ty.isEmpty()?"trichyeu="+ty.trim():"");
		
		Executions.getCurrent().sendRedirect("/vanban?"+param);
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	
}
