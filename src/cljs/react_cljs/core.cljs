(ns react-cljs.core
  (:require-macros [secretary.core :refer [defroute]])
  (:require [goog.events :as events]
            [goog.dom :as dom]
            [goog.history.EventType :as EventType]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [secretary.core :as secretary]
            [reagent.core :as reagent :refer [atom render]]
            [reagent.session :as session]
            [react-cljs.allforms :as records]
            [accountant.core :as accountant]
            )
  (:import goog.History))

(defn main-page []
  [:div.container
   [:div.page-header
    [:center
     [:h2 "Record Room Management System"]]]
   [:div.col-md-3
    [:ul.nav.nav-pills.nav-stacked
     [:li.active [:a {:data-toggle "pill" :href "#masavi"} "Masavi Records"]]
     [:li [:a {:data-toggle "pill" :href "#consolidation"} "Consoliation Records"]]
     [:li [:a {:data-toggle "pill" :href "#menu2"} "Menu 2"]]
     [:li [:a {:data-toggle "pill" :href "#menu3"} "Menu 3"]]]]
   [:div.col-md-9
    [:div.tab-content
     [:div#masavi.tab-pane.fade.in.active
      [:center [:h3 "Masavi Records"]]
      [records/masavi-table @records/masavi-dataset]]
     [:div#consolidation.tab-pane.fade
      [:center [:h3 "Consolidation Records"]]
      [records/consolidation-table @records/consolidation-dataset]
      ]
     [:div#menu2.tab-pane.fade [:h3 "Menu 2"]
      [:p "sdkgjjkkkj knjknds n vnefsjlmv"]]
     [:div#menu3.tab-pane.fade [:h3 "Menu 3"]
      [:p "asdl;sfdkgl;fhlklgj lkjldgjl;h"]]]]])


(defn home-page []
  [:div.container [main-page]])

(defn current-page []
  [:div [(session/get :current-page)]])


;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))

(init!)
