package vn.toancauxanh.cms.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.QVideo;
import vn.toancauxanh.gg.model.Video;
import vn.toancauxanh.service.BasicService;

public class VideoService extends BasicService<Video>{
	
	public JPAQuery<Video> getTargetQuery(){
		String tukhoa = MapUtils.getString(argDeco(), Labels.getLabel("param.tukhoa"));
		String trangthaisoan = MapUtils.getString(argDeco(),  Labels.getLabel("param.trangthaisoan"),"");
		JPAQuery<Video> q = find(Video.class);
		if(tukhoa!=null) {
			q.where(QVideo.video.tieuDe.like("%"+tukhoa+"%")
					.or(QVideo.video.moTa.like("%"+tukhoa+"%")));
		}
		if(trangthaisoan.equals("true")) {
			q.where(QVideo.video.xuatBan.eq(true));
		}else if(trangthaisoan.equals("false")) {
			q.where(QVideo.video.xuatBan.eq(false));
		}
		q.orderBy(QVideo.video.ngaySua.desc());
		return q;
	}
	public List<Video> getListVideoLienQuan(Long id){
		String chudevideo = MapUtils.getString(argDeco(), Labels.getLabel("param.chudevideo"));
		JPAQuery<Video> q = find(Video.class).where(QVideo.video.trangThai.ne(core().TT_KHONG_AP_DUNG));
		if(id!=null) {
			q.where(QVideo.video.id.ne(id));
		}
		if(chudevideo!=null) {
			try {
				q.where(QVideo.video.chuDeVideo.id.eq(Long.parseLong(chudevideo)));
			}catch(Exception ex) {
			}
		}
		q.limit(5);
		return q.fetch();
	}
	public Video getVideoNoiBat(){
		String chudevideo = MapUtils.getString(argDeco(), Labels.getLabel("param.chudevideo"));
		String id = MapUtils.getString(argDeco(), Labels.getLabel("param.idvideo"));
		JPAQuery<Video> q = find(Video.class);
		if(chudevideo!=null) {
			try {
				q.where(QVideo.video.chuDeVideo.id.eq(Long.parseLong(chudevideo)));
			}catch(Exception exx) {
			}
		}
		if(id!=null) {
			try {
				q.where(QVideo.video.id.eq(Long.parseLong(id)));
			}catch(Exception exx) {
			}
		}
		q.orderBy(QVideo.video.ngaySua.desc());
		return q.fetchFirst();
	}
	
}
