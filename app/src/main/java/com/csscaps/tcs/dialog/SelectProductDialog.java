package com.csscaps.tcs.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.common.utils.FastDoubleClickUtil;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.TCSApplication;
import com.csscaps.tcs.adapter.SelectProductListAdapter;
import com.csscaps.tcs.database.table.Product;
import com.csscaps.tcs.fragment.InvoiceProductListFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/5/24.
 */

public class SelectProductDialog extends DialogFragment implements Action1<Product> {

    @BindView(R.id.list_view)
    ListView mListView;
    @BindView(R.id.add_product)
    TextView mAddProduct;

    List<Product> data;
    SelectProductListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme3);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_product_dialog, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        int width = (int) (DeviceUtils.getScreenWidth(getContext()) * 0.7f);
        int height = (int) (DeviceUtils.getScreenHeight(getContext()) * 0.8f);
        dialogWindow.setLayout(width, height);
        dialogWindow.setWindowAnimations(R.style.scale_anim);
    }

    private void initView() {
        if (TCSApplication.currentUser.getRole() == 1) {
            mAddProduct.setVisibility(View.GONE);
        }
        ObserverActionUtils.addAction(this);
        data = select().from(Product.class).queryList();
        adapter = new SelectProductListAdapter(getContext(), R.layout.select_product_list_item_layout, data);
        mListView.setAdapter(adapter);
    }

    @OnClick({R.id.cancel, R.id.confirm, R.id.add_product})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.confirm:
                dismiss();
                List<Product> checkedList = adapter.getCheckedList();
                Subscription subscription = ObserverActionUtils.subscribe(checkedList.toArray(), InvoiceProductListFragment.class);
                if (subscription != null) subscription.unsubscribe();
                break;
            case R.id.add_product:
                if (FastDoubleClickUtil.isFastDoubleClick()) break;
                    AddProductDialog addProductDialog = new AddProductDialog();
                    addProductDialog.show(getChildFragmentManager(), "AddProductDialog");
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ObserverActionUtils.removeAction(this);
    }

    @Override
    public void call(Product product) {
        data.add(0, product);
        adapter.notifyDataSetChanged();
    }
}
