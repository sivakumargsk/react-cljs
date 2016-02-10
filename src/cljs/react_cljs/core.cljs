(ns react-cljs.core
  (:require [goog.events :as events]
            [goog.dom :as dom]
            [goog.history.EventType :as EventType]
            [goog.net.XhrIo :as xhr]
            [reagent.core :as reagent :refer [atom render]]
            [ajax.core :refer [GET  POST]]))


(def state (atom {:doc {} :saved? false}))

(defn set-value! [id value]
  (swap! state assoc :saved? false)
  (swap! state assoc-in [:doc id] value))

(defn get-value [id]
  (get-in @state [:doc id]))

;; ------------------------------------------------------------------
;; elements

(defn row [label & body]
  [:div.row
   [:div.col-md-2 [:label label]]
   [:div.col-md-3 body]])

(defn text-input [id label]
  [row label
   [:input.form-control {:type "text"
                         :value (get-value id)
                         :on-change #(set-value! id (-> % .-target .-value))}]])


(defn list-item [id k v selections]
  (letfn [(handle-click! []
            (swap! selections update-in [k] not)
            (set-value! id (->> @selections
                                (filter second)
                                (map first))))]
    [:li {:class (str "list-group-item"
                      (if (k @selections)  " active"))
          :on-click handle-click!} v]))

(defn selection-list [id label & items]
  (let [selections (->> items (map (fn [[k]] [k false])) (into {}) atom)]
    (fn []
      [:div.row
       [:div.col-md-2 [:label label]]
       [:div.col-md-5
        [:div.row
         (for [[k v] items]
           [list-item id k v selections])]]])))


(defn handler [response]
  (.log js/console (str response)))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))

(defn save-doc []
  (GET "/save"
        {:handler handler
         :error-handler error-handler}))

(defn save-doc1 []
  (GET "/save"
      {:handler (fn [res] (swap! state assoc :saved? res))
       :error-handler error-handler}))


(defn home []
  [:div.container
   [:div.page-header [:h1 "Cljs-ajax (Request and Response)"]]
   [:div.form-group
    [text-input :first-name "First name"]]
   [:div.form-group
    [text-input :last-name "Last Name"]]
   [:div.form-group
    [selection-list :favorite-drinks "Favorite drinks"
     [:coffee "Coffee"]
     [:beer "Beer"]
     [:crab-juice "Crab juice"]]]
     ;; [:div [:span (str @state)]]
   [:div.form-group
    (if (:saved? @state)
     [:b [:p "Saved"]]
     [:button.btn.btn-primary {:type "submit"
               :class "btn btn-default"
               :onClick save-doc1}
      "Submit"])]])


(defn render-home []
  (reagent/render-component [home]
                            (.getElementById js/document "app")))
(render-home)


