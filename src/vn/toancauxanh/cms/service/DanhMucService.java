package vn.toancauxanh.cms.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.TreeNode;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.DanhMuc;
import vn.toancauxanh.gg.model.QDanhMuc;
import vn.toancauxanh.service.BasicService;

public class DanhMucService extends BasicService<DanhMuc> {

	private DanhMuc danhMuc;

	private String img = "/backend/assets/img/edit.png";
	private String hoverImg = "/backend/assets/img/edit_hover.png";
	private String strUpdate = "Thứ tự";
	private boolean update = true;
	private boolean updateThanhCong = true;

	public DanhMuc getDanhMuc() {
		return danhMuc;
	}

	public void setDanhMuc(DanhMuc danhMuc) {
		this.danhMuc = danhMuc;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getHoverImg() {
		return hoverImg;
	}

	public void setHoverImg(String hoverImg) {
		this.hoverImg = hoverImg;
	}

	public String getStrUpdate() {
		return strUpdate;
	}

	public void setStrUpdate(String strUpdate) {
		this.strUpdate = strUpdate;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public boolean isUpdateThanhCong() {
		return updateThanhCong;
	}

	public void setUpdateThanhCong(boolean updateThanhCong) {
		this.updateThanhCong = updateThanhCong;
	}

	public void openObject(DefaultTreeModel<DanhMuc> model, TreeNode<DanhMuc> node) {
		if (node.isLeaf()) {
			model.addOpenObject(node);
		} else {
			for (TreeNode<DanhMuc> child : node.getChildren()) {
				model.addOpenObject(child);
				openObject(node.getModel(), child);
			}
		}
	}

	public DefaultTreeModel<DanhMuc> getModel() {

		String param = MapUtils.getString(argDeco(), "tukhoa", "").trim();
		String trangThai = MapUtils.getString(argDeco(), "trangthai", "");

		DanhMuc danhMucGoc = new DanhMuc();
		DefaultTreeModel<DanhMuc> model = new DefaultTreeModel<DanhMuc>(danhMucGoc.getNode(), true);
		for (DanhMuc danhMuc : getList()) {

			if ((danhMuc.getName().toLowerCase().contains(param.toLowerCase())
					&& danhMuc.getTrangThai().contains(trangThai)) || danhMuc.loadSizeChild() > 0) {
				danhMucGoc.getNode().add(danhMuc.getNode());
			}
		}

		if (!param.isEmpty() || !"".equals(param) || !trangThai.isEmpty() || !"".equals(trangThai)) {
			danhMucGoc.getNode().getModel().setOpenObjects(danhMucGoc.getNode().getChildren());
		}

		openObject(model, danhMucGoc.getNode());
		BindUtils.postNotifyChange(null, null, this, "sizeOfCategories");
		return model;
	}

	public List<DanhMuc> getList() {
		JPAQuery<DanhMuc> q = find(DanhMuc.class);
		q.where(QDanhMuc.danhMuc.trangThai.ne(core().TT_DA_XOA)).where(QDanhMuc.danhMuc.parent.isNull());
		q.orderBy(QDanhMuc.danhMuc.soThuTu.asc());
		List<DanhMuc> list = q.fetch();
		for (DanhMuc danhMuc : list) {
			danhMuc.loadChildren();
		}
		return list;
	}

	public List<DanhMuc> getDanhMucCon(DanhMuc danhMuc) {
		List<DanhMuc> list = new ArrayList<>();
		if (!danhMuc.getTrangThai().equalsIgnoreCase(core().TT_DA_XOA)) {
			for (TreeNode<DanhMuc> el : danhMuc.getNode().getChildren()) {
				list.add(el.getData());
				list.addAll(getDanhMucCon(el.getData()));
			}
		}
		return list;
	}

	// dùng để kiểm tra danh sách chủ đề
	// có rỗng không
	public long getSizeOfCategories() {
		JPAQuery<DanhMuc> q = find(DanhMuc.class).where(QDanhMuc.danhMuc.trangThai.ne(core().TT_DA_XOA));
		return q.fetchCount();
	}

	// =========================================

	public List<DanhMuc> getList2() {
		JPAQuery<DanhMuc> q = find(DanhMuc.class);
		q.where(QDanhMuc.danhMuc.trangThai.ne(core().TT_DA_XOA)).where(QDanhMuc.danhMuc.parent.isNull());
		q.orderBy(QDanhMuc.danhMuc.soThuTu.asc());
		List<DanhMuc> list = q.fetch();
		for (DanhMuc category : list) {
			category.loadChildren();
		}
		return list;
	}

	public List<DanhMuc> getListAllCategory() {

		List<DanhMuc> list = new ArrayList<>();
		for (DanhMuc danhMuc : getList2()) {
			list.add(danhMuc);
			list.addAll(getDanhMucCon(danhMuc));
		}
		return list;
	}

	public List<DanhMuc> getListAllCategoryAndNullButThis(DanhMuc self) {

		// nếu là thêm mới thì self bằng null
		List<DanhMuc> list = new ArrayList<>();
		list.add(null);
		for (DanhMuc cat : getListAllButThis(self)) {
			list.add(cat);
			list.addAll(getCategoryChildrenButThis(cat, self));
		}
		return list;
	}

	public List<DanhMuc> getListAllButThis(DanhMuc self) {
		JPAQuery<DanhMuc> q = find(DanhMuc.class);
		q.where(QDanhMuc.danhMuc.trangThai.ne(core().TT_DA_XOA)).where(QDanhMuc.danhMuc.parent.isNull());
		q.orderBy(QDanhMuc.danhMuc.soThuTu.asc());

		// không lấy lại chính nó (dùng khi chỉnh sửa)
		if (self != null && !self.noId()) {
			q.where(QDanhMuc.danhMuc.id.ne(self.getId()));
		}

		List<DanhMuc> list = q.fetch();

		for (DanhMuc danhMuc : list) {
			danhMuc.loadChildren();
		}
		return list;
	}

	// bỏ qua, không lấy thằng cha của nó
	public List<DanhMuc> getCategoryChildrenButThis(DanhMuc danhMuc, DanhMuc ignore) {
		List<DanhMuc> list = new ArrayList<>();
		if (!danhMuc.getTrangThai().equals(core().TT_DA_XOA)) {
			for (TreeNode<DanhMuc> el : danhMuc.getNode().getChildren()) {
				if (ignore.getId() != el.getData().getId()) {
					list.add(el.getData());
					list.addAll(getCategoryChildrenButThis(el.getData(), ignore));
				}
			}
		}
		return list;
	}

	// =================================================================================================

	public void fixSoThuTu() {
		int i = 1;
		for (DanhMuc category : getList()) {
			category.setSoThuTu(i);
			category.save();
			int j = 1;
			for (DanhMuc cat : getDanhMucCon(category)) {
				if (cat.getParent().equals(category)) {
					cat.setSoThuTu(j);
					cat.save();
					int idx = 1;
					for (DanhMuc c : getDanhMucCon(cat)) {
						if (c.getParent().equals(cat)) {
							c.setSoThuTu(idx);
							c.save();
							int k = 1;
							for (DanhMuc a : getDanhMucCon(c)) {
								if (a.getParent().equals(c)) {
									a.setSoThuTu(k);
									a.save();
									k++;
								}
							}
							idx++;
						}
					}
					j++;
				}
			}
			i++;
		}
	}

	@Command
	public void clickButton(@BindingParam("model") final List<DanhMuc> model) {
		if (strUpdate.equals("Thứ tự")) {
			setStrUpdate("Lưu thứ tự");
			setImg("/backend/assets/img/save.png");
			setHoverImg("/backend/assets/img/save_hover.png");
			setUpdate(false);
		} else {
			setUpdateThanhCong(true);

			if (model == null) {
				for (DanhMuc cat : listChuDeThayDoiThuTu) {
					if (check(cat)) {
						setUpdateThanhCong(false);
						break;
					}
					cat.save();
				}
			} else {
				for (DanhMuc cat : model) {
					if (check(cat)) {
						setUpdateThanhCong(false);
						break;
					}
					cat.save();
				}
			}

			if (isUpdateThanhCong()) {
				setStrUpdate("Thứ tự");
				setImg("/backend/assets/img/edit.png");
				setHoverImg("/backend/assets/img/edit_hover.png");
				setUpdate(true);
			} else {
				setUpdateThanhCong(updateThanhCong);
			}
		}
		BindUtils.postNotifyChange(null, null, this, "img");
		BindUtils.postNotifyChange(null, null, this, "hoverImg");
		BindUtils.postNotifyChange(null, null, this, "update");
		BindUtils.postNotifyChange(null, null, this, "strUpdate");
		BindUtils.postNotifyChange(null, null, this, "updateThanhCong");
		BindUtils.postNotifyChange(null, null, this, "list");
		BindUtils.postNotifyChange(null, null, this, "model");
		BindUtils.postNotifyChange(null, null, this, "targetQueryTheLoai");
	}

	private boolean check(DanhMuc cat) {
		if (cat.getSoThuTu() <= 0) {
			return true;
		}
		return false;
	}

	private List<DanhMuc> listChuDeThayDoiThuTu = new ArrayList<>();

	public List<DanhMuc> getListChuDeThayDoiThuTu() {
		return listChuDeThayDoiThuTu;
	}

	public void setListChuDeThayDoiThuTu(List<DanhMuc> listChuDeThayDoiThuTu) {
		this.listChuDeThayDoiThuTu = listChuDeThayDoiThuTu;
	}

	public void addListChuDeThayDoiThuTu(DanhMuc category, int stt) {
		if (listChuDeThayDoiThuTu.contains(category)) {
			listChuDeThayDoiThuTu.remove(category);
			category.setSoThuTu(stt);
			listChuDeThayDoiThuTu.add(category);
		} else {
			category.setSoThuTu(stt);
			listChuDeThayDoiThuTu.add(category);
		}
	}

	// ===========================================================================

	public List<DanhMuc> getListAllCategoryAndNull() {
		List<DanhMuc> list = new ArrayList<>();
		list.add(null);
		for (DanhMuc danhMuc : getList()) {
			list.add(danhMuc);
			list.addAll(getDanhMucCon(danhMuc));
		}
		return list;
	}

	// ================================================================
	public JPAQuery<DanhMuc> getById(Long id) {
		if (id != null && id > 0) {
			JPAQuery<DanhMuc> q = find(DanhMuc.class).where(QDanhMuc.danhMuc.trangThai.ne(core().TT_DA_XOA)
					.and(QDanhMuc.danhMuc.id.eq(id)).and(QDanhMuc.danhMuc.trangThai.eq(core().TT_AP_DUNG)));
			return q.orderBy(QDanhMuc.danhMuc.soThuTu.asc());
		}

		return find(DanhMuc.class).where(QDanhMuc.danhMuc.id.eq(0l));
	}

	public JPAQuery<DanhMuc> getChild(Long parentId) {
		if (parentId != null && parentId > 0) {
			JPAQuery<DanhMuc> q = find(DanhMuc.class)
					.where(QDanhMuc.danhMuc.trangThai.ne(core().TT_DA_XOA).and(QDanhMuc.danhMuc.parent.id.eq(parentId))
							.and(QDanhMuc.danhMuc.trangThai.eq(core().TT_AP_DUNG)));
			return q.orderBy(QDanhMuc.danhMuc.soThuTu.asc());
		}
		return find(DanhMuc.class).where(QDanhMuc.danhMuc.id.eq(0l));
	}
}
