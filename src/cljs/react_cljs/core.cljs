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
      [records/o4register-table @records/o4register-dataset]]
     [:div#o6register.tab-pane.fade
      [:center [:h3 "O6 Register"]]
      [records/o6register-table @records/o6register-dataset]]
     [:div#mutation.tab-pane.fade
      [:center [:h3 "Mutation Records"]]
      [records/mutation-table @records/mutation-dataset]]]]])


(defn home-page []
  [:div [main-page]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; home-Routes
(defroute "/" []
  (session/put! :current-page #'home-page))


;; ------ masavi ------------

(defroute "/masavi/add" []
  (session/put! :current-page #'records/masavi-add-record))

(defroute "/masavi/update" []
  (session/put! :current-page #'records/masavi-upd-record))



;; ------- consolidation -------
(defroute "/consolidation/add" []
  (session/put! :current-page #'records/consolidation-add-record))

(defroute "/consolidation/update" []
  (session/put! :current-page #'records/consolidation-upd-record))


;; ------- filed form-----------
(defroute "/fieldform/add" []
  (session/put! :current-page #'records/fieldform-add-record))

(defroute "/fieldform/update" []
  (session/put! :current-page #'records/fieldform-upd-record))


;; ---------Khasragirdwani-------
(defroute "/khasragirdwani/add" []
  (session/put! :current-page #'records/khasragirdwani-add-record))

(defroute "/khasragirdwani/update" []
  (session/put! :current-page #'records/khasragirdwani-upd-record))

;; --------Revenuerecord---------
(defroute "/revenuerecord/add" []
  (session/put! :current-page #'records/revenuerecord-add-record))

(defroute "/revenuerecord/update" []
  (session/put! :current-page #'records/revenuerecord-upd-record))

;; -------misc-----------------
(defroute "/misc/add" []
  (session/put! :current-page #'records/misc-add-record))

(defroute "/misc/update" []
  (session/put! :current-page #'records/misc-upd-record))

;; -------o2 register----------
(defroute "/o2register/add" []
  (session/put! :current-page #'records/o2register-add-record))

(defroute "/o2register/update" []
  (session/put! :current-page #'records/o2register-upd-record))

;; --------o4 register---------
(defroute "/o4register/add" []
  (session/put! :current-page #'records/o4register-add-record))

(defroute "/o4register/update" []
  (session/put! :current-page #'records/o4register-upd-record))

;; -------o6 register----------
(defroute "/o6register/add" []
  (session/put! :current-page #'records/o6register-add-record))

(defroute "/o6register/update" []
  (session/put! :current-page #'records/o6register-upd-record))

;; ------mutation -------------
(defroute "/mutation/add" []
  (session/put! :current-page #'records/mutation-add-record))

(defroute "/mutation/update" []
  (session/put! :current-page #'records/mutation-upd-record))


;; -------rendering part---------------
(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))

(init!)
