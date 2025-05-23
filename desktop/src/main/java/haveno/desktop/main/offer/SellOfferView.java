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

package haveno.desktop.main.offer;

import com.google.inject.Inject;

import haveno.core.locale.Res;
import haveno.core.offer.OfferDirection;
import haveno.core.user.Preferences;
import haveno.core.user.User;
import haveno.desktop.Navigation;
import haveno.desktop.common.view.FxmlView;
import haveno.desktop.common.view.ViewLoader;
import haveno.network.p2p.P2PService;

@FxmlView
public class SellOfferView extends OfferView {

    @Inject
    public SellOfferView(ViewLoader viewLoader,
                         Navigation navigation,
                         Preferences preferences,
                         User user,
                         P2PService p2PService) {
        super(viewLoader,
                navigation,
                preferences,
                user,
                p2PService,
                OfferDirection.SELL);
    }

    @Override
    protected String getOfferLabel() {
        return Res.get("offerbook.sellXmrFor");
    }
}
