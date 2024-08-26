(ns giggin.components.orders 
  (:require [giggin.state :as state]
            [giggin.helpers :refer [format-price]]))

(defn total
  []
  (->> @state/orders
       (map (fn [[id cant]]
              (* (get-in @state/gigs [id :price]) cant)))
       (apply +)))

(defn orders
  []
  [:aside
   (if (empty? @state/orders)
     [:div.empty
      [:div.title "You do not have any orders"]
      [:div.subtitle "Click on a + to add an order"]]
     [:div.order
      [:div.body
       (for [[id cant] @state/orders]
         (let [[image titulo precio] ((juxt :img :title :price) (-> @state/gigs id))]
           [:div.item {:key id}
            [:div.img
             [:img {:src image
                    :alt titulo}]]
            [:div.content
             [:p.title (str titulo " " \u00D7 cant)]]
            [:div.action
             [:div.price (format-price (* precio cant))]
             [:button.btn.btn--link.tooltip {:data-tooltip "Remove"
                                             :on-click #(swap! state/orders dissoc id)}
              [:i.icon.icon--cross]]]]))]
      [:div.total
       [:hr]
       [:div.item
        [:div.content "Total"]
        [:div.action
         [:div.price (format-price (total))]]
        [:button.btn.btn--link.tooltip {:data-tooltip "Remove all"
                                        :on-click #(reset! state/orders {})}
         [:i.icon.icon--delete]]]]])])

 