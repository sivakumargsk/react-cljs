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
  [:div
   [:div.page-header
    [:center
     [:h2 "Record Room Management System"]]]
   [:div.col-md-2
    [:ul.nav.nav-pills.nav-stacked
     [:li.active [:a {:data-toggle "pill" :href "#masavi"} "Masavi Records"]]
     [:li [:a {:data-toggle "pill" :href "#consolidation"} "Consoliation Records"]]
     [:li [:a {:data-toggle "pill" :href "#field"} "Filed Form"]]
     [:li [:a {:data-toggle "pill" :href "#khasragirdwani"} "Khasra Girdwani"]]
     [:li [:a {:data-toggle "pill" :href "#revenuerecord"} "Revenue Record"]]
     [:li [:a {:data-toggle "pill" :href "#misc"} "Misc Records"]]
     [:li [:a {:data-toggle "pill" :href "#o2register"} "O2 Register"]]
     [:li [:a {:data-toggle "pill" :href "#o4register"} "O4 Register"]]
     [:li [:a {:data-toggle "pill" :href "#o6register"} "O6 Register"]]
     [:li [:a {:data-toggle "pill" :href "#mutation"} "Mutation Records"]]]]
   [:div.col-md-10
    [:div.tab-content
     [:div#masavi.tab-pane.fade.in.active
      [:center [:h3 "Masavi Records"]]
      [records/masavi-table @records/masavi-dataset]]
     [:div#consolidation.tab-pane.fade
      [:center [:h3 "Consolidation Records"]]
      [records/consolidation-table @records/consolidation-dataset]]
     [:div#field.tab-pane.fade
      [:center  [:h3 "Field Form"]]
      [records/fieldform-table @records/fieldform-dataset]]
     [:div#khasragirdwani.tab-pane.fade
      [:center [:h3 "Khasra Girdwani"]]
      [records/khasragirdwani-table @records/khasragirdwani-dataset]]
     [:div#revenuerecord.tab-pane.fade
      [:center [:h3 "Revenue Record"]]
      [records/revenuerecord-table @records/revenuerecord-dataset]]
     [:div#misc.tab-pane.fade
      [:center [:h3 "Misc Records"]]
      [records/misc-table @records/misc-dataset]]
     [:div#o2register.tab-pane.fade
      [:center [:h3 "O2 Register"]]
      [records/o2register-table @records/o2register-dataset]]
     [:div#o4register.tab-pane.fade
      [:center [:h3 "O4 Register"]]
      [records/o4register-table @records/o4register-dataset]
      ]
     [:div#o6register.tab-pane.fade
      [:center [:h3 "O6 Register"]]
      [records/o6register-table @records/o6register-dataset]]
     [:div#mutation.tab-pane.fade
      [:center [:h3 "Mutation Records"]]
      [records/mutation-table @records/mutation-dataset]
      ]
     ]]])


(defn home-page []
  [:div [main-page]])


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
