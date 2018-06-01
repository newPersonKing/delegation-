package adpterimpl;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.gy.delegationadapter.AbsDelegationAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends AbsDelegationAdapter<BaseViewHolder> {

    private List<Object> mDataItems = new ArrayList<>();
    private List<Object> mHeaderItems = new ArrayList<>();
    private List<Object> mFooterItems = new ArrayList<>();


    @Override
    protected Object getItem(int position) {
        if (position < mHeaderItems.size()) {
            return mHeaderItems.get(position);
        }

        position -= mHeaderItems.size();
        if (position < getDataCount()) {
            return mDataItems.get(position);
        }

        position -= mDataItems.size();
        if (position < mFooterItems.size()) {
            return mFooterItems.get(position);
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getDataCount() + getFooterCount();
    }


    public void setHeaderItem(Object headerItem) {
        mHeaderItems.clear();
        mHeaderItems.add(headerItem);
        notifyDataSetChanged();
    }

    public void addHeaderItem(Object headerItem) {
        addHeaderItem(getHeaderCount(), headerItem);
    }

    public void addHeaderItem(int position, Object headerItem) {
        mHeaderItems.add(position, headerItem);
        notifyItemRangeInserted(position, 1);
    }

    public void setFooterItems(List footerItems) {
        mFooterItems = footerItems;
        notifyDataSetChanged();
    }

    public void setFooterItem(Object footerItem) {
        mFooterItems.clear();
        mFooterItems.add(footerItem);
        notifyDataSetChanged();
    }

    public void addFooterItem(Object footerItem) {
        addFooterItem(getFooterCount(), footerItem);
    }

    public void addFooterItem(int position, Object footerItem) {
        mFooterItems.add(position, footerItem);
        notifyItemRangeInserted(getHeaderCount() + getDataCount() + position, 1);
    }

    public void setDataItems(List dataItems) {
        mDataItems = dataItems;
        notifyDataSetChanged();
    }

    public void addDataItem(Object item) {
        addDataItem(getDataCount(), item);
    }

    public void addDataItem(int position, Object item) {
        mDataItems.add(position, item);
        notifyItemRangeInserted(getHeaderCount() + position, 1);
    }

    public void addDataItems(List dataItems) {
        addDataItems(getDataCount(), dataItems);
    }

    public void addDataItems(int position, List dataItems) {
        mDataItems.addAll(position, dataItems);
        notifyItemRangeInserted(getHeaderCount() + position , dataItems.size());
    }

    public void moveDataItem(int fromPosition, int toPosition) {
        toPosition = fromPosition < toPosition ? toPosition - 1 : toPosition;
        mDataItems.add(toPosition, mDataItems.remove(fromPosition));
        notifyItemMoved(fromPosition, toPosition);
    }

    public void removeDataItem(Object dataItem) {
        int index = mDataItems.indexOf(dataItem);
        if (index != -1 && index <= getDataCount()) {
            removeDataItem(index);
        }
    }


    public void removeDataItem(int position) {
        removeDataItem(position, 1);
    }

    public void removeDataItem(int position, int itemCount) {
        for (int i = 0; i < itemCount; i++) {
            mDataItems.remove(position);
        }
        notifyItemRangeRemoved(getHeaderCount() + position , itemCount);
    }

    public List<Object> getDataList() {
        return mDataItems;
    }

    public List<Object> getHeaderList() {
        return mHeaderItems;
    }

    public List<Object> getFooterItems() {
        return mFooterItems;
    }



    public int getDataCount() {
        return mDataItems.size();
    }

    public int getHeaderCount() {
        return mHeaderItems.size();
    }

    public int getFooterCount() {
        return mFooterItems.size();
    }

    public void clearData() {
        mDataItems.clear();
    }

    public void clearHeader() {
        mHeaderItems.clear();
    }

    public void clearFooter() {
        mFooterItems.clear();
    }

    public void clearAllData() {
        clearData();
        clearHeader();
        clearFooter();
    }

    public interface OnItemChildLongClickListener {

        boolean onItemChildLongClick(MyAdapter adapter, View view, int position);
    }

    public interface OnItemClickListener {

        void onItemClick(MyAdapter adapter, View view, int position);
    }

    public interface OnItemLongClickListener {

        boolean onItemLongClick(MyAdapter adapter, View view, int position);
    }

    @Override
    public void bindViewClickListener(View itemView, final int position) {

        if (getOnItemClickListener() != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getOnItemClickListener().onItemClick(MyAdapter.this, v,position);
                }
            });
        }
        if (getOnItemLongClickListener() != null) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return getOnItemLongClickListener().onItemLongClick(MyAdapter.this, v,position);
                }
            });
        }
    }
}
