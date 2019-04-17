package com.ps.xh.facefile.main;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ps.xh.facefile.R;

import java.util.List;

public class LockFileAdapter extends BaseQuickAdapter<FileBean, BaseViewHolder> {
    public LockFileAdapter(@Nullable List<FileBean> data) {
        super(R.layout.item_lock_file,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FileBean item) {
        helper.setText(R.id.img_item_recycler_neme,item.getName());
        if (item.isLock()){
            helper.setImageResource(R.id.img_item_recycler_file,R.mipmap.icon_item_lock);
        }else {
            helper.setImageResource(R.id.img_item_recycler_file,R.mipmap.icon_main_file_nolock);
        }
    }
}
