package com.moyacs.canary.moudle.guide;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.moyacs.canary.base.BaseActivity;
import com.moyacs.canary.main.MainActivity;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import www.moyacs.com.myapplication.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/3/7
 * @email 252774645@qq.com
 */
public class GuideActivity extends BaseActivity implements GuideContract.View {

    @BindView(R.id.vp_guide)
    ViewPager vpGuide;
    private GuideContract.Presenter mPresenter;
    private GuideViewPagerAdapter mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initView() {
        new GuidePresenter(this);
        mAdapter = new GuideViewPagerAdapter(GuideActivity.this, new Integer[0], mPagerItemListener);
        vpGuide.setAdapter(mAdapter);
        mPresenter.loadViews();
    }

    @Override
    protected void intListener() {

    }

    @Override
    protected void initData() {

    }


    @Override
    public void jumpHomeActivity() {
        startActivity(new Intent(GuideActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void showViewPager(Integer[] pics) {
        mAdapter.replaceData(pics);
    }


    @Override
    public void setPresenter(GuideContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showMessageTips(String msg) {

    }


    private PagerItemListener mPagerItemListener = new PagerItemListener() {

        @Override
        public void onBtnClick() {
            jumpHomeActivity();
        }
    };


    private class GuideViewPagerAdapter extends PagerAdapter {
        private List<View> mViews;
        private PagerItemListener mPagerItemListener;
        private Context mContext;

        public GuideViewPagerAdapter(Context context, Integer[] viewIDs, PagerItemListener pagerItemListener) {
            super();

            mPagerItemListener = pagerItemListener;
            mContext = context;
            initList(viewIDs);
        }

        @Override
        public int getCount() {
            if (mViews != null) {
                return mViews.size();
            }
            return 0;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(mViews.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(mViews.get(position), 0);
            return mViews.get(position);
        }

        public void replaceData(Integer[] viewIDs) {
            initList(viewIDs);
            notifyDataSetChanged();
        }

        public void initList(Integer[] viewIDs) {

            mViews = new ArrayList<View>();
            Observable.fromArray(viewIDs).map(new Function<Integer, View>() {
                @Override
                public View apply(Integer integer) throws Exception {
                    return LayoutInflater.from(mContext).inflate(integer, null);
                }
            }).subscribe(new Observer<View>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(View view) {
                    mViews.add(view);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {
                    if (!mViews.isEmpty() && mViews.size() > 1) {
                        //最后一个布局需要带button
                        Button startBtn = (Button) mViews.get(mViews.size() - 1).findViewById(R.id.btn_enter);
                        startBtn.setTag("enter");
                        startBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mPagerItemListener.onBtnClick();
                            }
                        });

                    }
                }
            });

        }
    }

    public interface PagerItemListener {
        void onBtnClick();
    }
}
