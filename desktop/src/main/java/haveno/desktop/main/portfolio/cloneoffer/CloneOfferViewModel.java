/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package haveno.desktop.main.portfolio.cloneoffer;

import haveno.desktop.Navigation;
import haveno.desktop.main.offer.MutableOfferViewModel;
import haveno.desktop.main.offer.OfferViewUtil;

import haveno.core.account.witness.AccountAgeWitnessService;
import haveno.core.offer.OfferUtil;
import haveno.core.offer.OpenOffer;
import haveno.core.payment.validation.FiatVolumeValidator;
import haveno.core.payment.validation.SecurityDepositValidator;
import haveno.core.payment.validation.XmrValidator;
import haveno.core.provider.price.PriceFeedService;
import haveno.core.user.Preferences;
import haveno.core.util.FormattingUtils;
import haveno.core.util.PriceUtil;
import haveno.core.util.coin.CoinFormatter;
import haveno.core.util.validation.AmountValidator4Decimals;
import haveno.core.util.validation.AmountValidator8Decimals;
import haveno.common.handlers.ErrorMessageHandler;
import haveno.common.handlers.ResultHandler;

import com.google.inject.Inject;
import com.google.inject.name.Named;

class CloneOfferViewModel extends MutableOfferViewModel<CloneOfferDataModel> {

    @Inject
    public CloneOfferViewModel(CloneOfferDataModel dataModel,
                            FiatVolumeValidator fiatVolumeValidator,
                            AmountValidator4Decimals priceValidator4Decimals,
                            AmountValidator8Decimals priceValidator8Decimals,
                            XmrValidator xmrValidator,
                            SecurityDepositValidator securityDepositValidator,
                            PriceFeedService priceFeedService,
                            AccountAgeWitnessService accountAgeWitnessService,
                            Navigation navigation,
                            Preferences preferences,
                            @Named(FormattingUtils.BTC_FORMATTER_KEY) CoinFormatter btcFormatter,
                            OfferUtil offerUtil) {
        super(dataModel,
                fiatVolumeValidator,
                priceValidator4Decimals,
                priceValidator8Decimals,
                xmrValidator,
                securityDepositValidator,
                priceFeedService,
                accountAgeWitnessService,
                navigation,
                preferences,
                btcFormatter,
                offerUtil);
        syncMinAmountWithAmount = false;
    }

    @Override
    public void activate() {
        super.activate();

        dataModel.populateData();

        long triggerPriceAsLong = dataModel.getTriggerPrice();
        dataModel.setTriggerPrice(triggerPriceAsLong);
        if (triggerPriceAsLong > 0) {
            triggerPrice.set(PriceUtil.formatMarketPrice(triggerPriceAsLong, dataModel.getCurrencyCode()));
        } else {
            triggerPrice.set("");
        }
        onTriggerPriceTextFieldChanged();
    }

    public void applyOpenOffer(OpenOffer openOffer) {
        dataModel.reset();
        dataModel.applyOpenOffer(openOffer);
    }

    public void onCloneOffer(ResultHandler resultHandler, ErrorMessageHandler errorMessageHandler) {
        dataModel.onCloneOffer(resultHandler, errorMessageHandler);
    }

    public void onInvalidateMarketPriceMargin() {
        marketPriceMargin.set(FormattingUtils.formatToPercent(dataModel.getMarketPriceMarginPct()));
    }

    public void onInvalidatePrice() {
        price.set(FormattingUtils.formatPrice(null));
        price.set(FormattingUtils.formatPrice(dataModel.getPrice().get()));
    }

    public boolean isSecurityDepositValid() {
        return securityDepositValidator.validate(securityDeposit.get()).isValid;
    }

    @Override
    public void triggerFocusOutOnAmountFields() {
        // do not update BTC Amount or minAmount here
        // issue 2798: "after a few edits of offer the BTC amount has increased"
    }

    public boolean isShownAsSellOffer() {
        return OfferViewUtil.isShownAsSellOffer(getTradeCurrency(), dataModel.getDirection());
    }
}