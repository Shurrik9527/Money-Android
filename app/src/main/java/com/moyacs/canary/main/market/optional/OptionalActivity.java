package com.moyacs.canary.main.market.optional;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import com.moyacs.canary.base.BaseActivity;
import com.moyacs.canary.bean.TradeVo;
import com.moyacs.canary.bean.event.EvenVo;
import com.moyacs.canary.util.ScreenUtil;
import com.moyacs.canary.widget.UnderLineTextView;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 自选行情
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class OptionalActivity extends BaseActivity implements OptionalContract.View{

    @BindView(R.id.tv_myOptional)
    RecyclerView rvMyOptional;
    @BindView(R.id.tv_1)
    UnderLineTextView tv1;
    @BindView(R.id.tv_2)
    UnderLineTextView tv2;
    @BindView(R.id.tv_3)
    UnderLineTextView tv3;
    @BindView(R.id.rv_optional)
    RecyclerView rvOptional;

    private UnderLineTextView oldSelectView;
    private OptionalAdapter myOptionalAdapter;
    private OptionalAdapter optionalAdapter;
    private OptionalPresenter presenter;
    private List<TradeVo.Trade> waiHuiList;
    private List<TradeVo.Trade> guiJinShuList;
    private List<TradeVo.Trade> yuanYouList;
    private List<TradeVo.Trade> myChoiceList;
    private List<TradeVo.Trade> optionalList;
    private int selectPos;// 选择的位置
    private TradeVo.Trade targetTrade; // 操作目标的外汇 也许是删除 也许是添加
    private int targetPos;//操作目标的位置
    private boolean changeMyChoice; //是否改变了我的选择  用于是否刷新Fragment我的自选列表

    @Override
    protected int getLayoutId() {
        return R.layout.activity_optional;
    }

    @Override
    protected void initView() {
        myChoiceList = new ArrayList<>();
        waiHuiList = new ArrayList<>();
        guiJinShuList = new ArrayList<>();
        yuanYouList = new ArrayList<>();
        optionalList = new ArrayList<>();
        myOptionalAdapter = new OptionalAdapter(true, myChoiceList);
        rvMyOptional.setLayoutManager(new GridLayoutManager(this, 3, LinearLayout.VERTICAL, false));
        rvMyOptional.addItemDecoration(new MyItemDecoration());
        rvMyOptional.setAdapter(myOptionalAdapter);

        optionalAdapter = new OptionalAdapter(false, optionalList);
        rvOptional.setLayoutManager(new GridLayoutManager(this, 3, LinearLayout.VERTICAL, false));
        rvOptional.addItemDecoration(new MyItemDecoration());
        rvOptional.setAdapter(optionalAdapter);
        onViewClicked(tv1);
    }

    @Override
    protected void intListener() {
        myOptionalAdapter.setItemClickListener((view, pos) -> {
            targetTrade = myChoiceList.get(pos);
            targetPos = pos;
            presenter.deleteOptional(myChoiceList.get(targetPos).getSymbolCode());
        });
        optionalAdapter.setItemClickListener((view, pos) -> {
            //添加自选
            if (selectPos == 1) {
                targetTrade = waiHuiList.get(pos);
            } else if (selectPos == 2) {
                targetTrade = guiJinShuList.get(pos);
            } else if (selectPos == 3) {
                targetTrade = yuanYouList.get(pos);
            }
            targetPos = pos;
            presenter.addOptional(targetTrade.getSymbolCode());
        });
    }

    @Override
    protected void initData() {
        presenter = new OptionalPresenter(this);
        presenter.getMyChoice();
    }

    @OnClick({R.id.iv_break, R.id.tv_1, R.id.tv_2, R.id.tv_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_break:
                finish();
                break;
            case R.id.tv_1:
                setSelectTabView(tv1);
                selectPos = 1;
                replaceOptionalList();
                break;
            case R.id.tv_2:
                setSelectTabView(tv2);
                selectPos = 2;
                replaceOptionalList();
                break;
            case R.id.tv_3:
                setSelectTabView(tv3);
                selectPos = 3;
                replaceOptionalList();
                break;
        }
    }

    private void setSelectTabView(UnderLineTextView selectTabView) {
        if (oldSelectView != null) {
            oldSelectView.setSelected(false);
        }
        selectTabView.setSelected(true);
        oldSelectView = selectTabView;
    }

    @Override
    public void setMyChoice(List<TradeVo.Trade> tradeList) {
        myChoiceList.addAll(tradeList);
        myOptionalAdapter.notifyDataSetChanged();
        presenter.getOptionalList(); //获取可选择列表
    }

    @Override
    public void setOptionalList(List<TradeVo.Trade> tradeList) {
        for (TradeVo.Trade t : tradeList) {
            boolean isBreak = false;
            for (TradeVo.Trade my : myChoiceList) {
                if (TextUtils.equals(t.getSymbolCode(), my.getSymbolCode())) {
                    isBreak = true;
                    break;
                }
            }
            if (!isBreak) {
                if (t.getSymbolType() == 1) {
                    //外汇
                    waiHuiList.add(t);
                } else if (t.getSymbolType() == 2) {
                    //贵金属
                    guiJinShuList.add(t);
                } else if (t.getSymbolType() == 3) {
                    //原油
                    yuanYouList.add(t);
                }
            }
        }
        replaceOptionalList();
    }

    @Override
    public void addOptionalSuccess() {
        myChoiceList.add(targetTrade);
        myOptionalAdapter.notifyDataSetChanged();
        if (selectPos == 1) {
            waiHuiList.remove(targetPos);
        } else if (selectPos == 2) {
            guiJinShuList.remove(targetPos);
        } else if (selectPos == 3) {
            yuanYouList.remove(targetPos);
        }
        replaceOptionalList();
        changeMyChoice = true;
    }

    @Override
    public void deleteOptionalSuccess() {
        myChoiceList.remove(targetPos);
        myOptionalAdapter.notifyDataSetChanged();
        if (selectPos == 1) {
            waiHuiList.add(targetTrade);
        } else if (selectPos == 2) {
            guiJinShuList.add(targetTrade);
        } else if (selectPos == 3) {
            yuanYouList.add(targetTrade);
        }
        replaceOptionalList();
        changeMyChoice = true;
    }

    private void replaceOptionalList() {
        optionalList.clear();
        if (selectPos == 1) {
            optionalList.addAll(waiHuiList);
        } else if (selectPos == 2) {
            optionalList.addAll(guiJinShuList);
        } else if (selectPos == 3) {
            optionalList.addAll(yuanYouList);
        }
        optionalAdapter.notifyDataSetChanged();
    }

    private class MyItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int pos = parent.getChildAdapterPosition(view);
            int offset = ScreenUtil.dip2px(OptionalActivity.this, 16);
            int itemContent = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            if ((pos + 1) <= itemContent) {
                outRect.top = offset;
            }
            if ((pos + 1) % itemContent == 0) {
                outRect.right = offset;
            }
            outRect.bottom = offset;
            outRect.left = offset;
        }
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        presenter.unsubscribe();
    }

    @Override
    protected void onDestroy() {
        senChangeMyChoiceEvent();
        super.onDestroy();
    }

    private void senChangeMyChoiceEvent(){
        EventBus.getDefault().post(new EvenVo(EvenVo.UPDATE_MY_CHOICE));
    }
}
