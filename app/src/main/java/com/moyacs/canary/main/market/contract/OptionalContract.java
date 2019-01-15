package com.moyacs.canary.main.market.contract;

import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseView;
import com.moyacs.canary.main.market.net.TradeVo;

import java.util.List;

public interface OptionalContract {
    interface View extends BaseView {
        void setMyChoice(List<TradeVo.Trade> tradeList);

        void setOptionalList(List<TradeVo.Trade> tradeList);

        void addOptionalSuccess();

        void deleteOptionalSuccess();
    }

    interface Presenter extends BasePresenter {
        void getMyChoice();

        void getOptionalList();

        void addOptional(String symbolCode);

        void deleteOptional(String symbolCode);
    }
}
