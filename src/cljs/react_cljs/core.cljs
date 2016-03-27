(ns react-cljs.core
  (:require-macros [secretary.core :refer [defroute]])
  (:require [goog.events :as events]
            [goog.dom :as dom]
            [goog.history.EventType :as EventType]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [secretary.core :as secretary]
            [reagent.core :as reagent :refer [atom render]]
            [cljsjs.react-bootstrap])
  (:import goog.History))

;; =============================== Button ===============================================

(def button-tool-bar (reagent/adapt-react-class (aget js/ReactBootstrap "ButtonToolbar")))
(def button (reagent/adapt-react-class (aget js/ReactBootstrap "Button")))

(defn buttons []
  [:div
   [:div.page-header [:h3[:b "Buttons"]]]
   [button-tool-bar
    [button "Default"]
    [button {:bs-style "primary"} "Primary"]
    [button {:bs-style "success"} "Success"]
    [button {:bs-style "info"} "info"]
    [button {:bs-style "warning"} "Warning"]
    [button {:bs-style "danger"} "Danger"]
    [button {:bs-style "link"} "Link"]]])


;; ================================ Button-Size ==========================================
(defn buttons-size []
  [:div
   [:div.page-header [:h3 [:b "Button-size"]]]
   [:div
    [button-tool-bar
     [button {:bs-style "primary"
              :bs-size "large"} "Large button"]
     [button {:bs-size "large"} "Large button"]]
    [button-tool-bar
     [button {:bs-style "primary"} "Default button"]
     [button  "Default button"]]
    [button-tool-bar
     [button {:bs-style "primary"
              :bs-size "small"} "Small button"]
     [button {:bs-size "small"} "small button"]]
    [button-tool-bar
     [button {:bs-style "primary"
              :bs-size "xsmall"} "Extra small button"]
     [button {:bs-size "xsmall"} "Extra small button"]]]])

;; ================================== Block-level-buttons ====================================

(defn block-level-buttons []
  [:div
   [:div.page-header [:h3 [:b "Block-level-Buttons"]]]
   [:div {:class "well"
          :style {:max-width 400 :margin "0 auto 10px"}}
    [button {:bs-style "primary" :bs-size "large" :block true}  "Block level button"]
    [button {:bs-size "large" :block true} "Block level buttons"]]])

;; ==================================== Buttons-Active-State ==================================

(defn active-buttons []
  [:div
   [:div.page-header [:h3 [:b "Active-Button"]]]
   [button-tool-bar
    [button {:bs-style "primary" :active true}  "Primary button"]
    [button {:active true} "Button"]]])

;; ==================================== Disabled Buttons =======================================

(defn disabled-buttons []
  [:div
   [:div.page-header [:h3 [:b "Disabled-Buttons"]]]
   [button-tool-bar
    [button {:bs-style "primary" :disabled true}  "Primary button"]
    [button {:disabled true} "Button"]]])

;; ===================================== Button tags ============================================

(defn button-tag []
  [:div
   [:div.page-header [:h3 [:b "Button-tag"]]]
   [button-tool-bar
    [button {:href "#"}  "link"]
    [button "Button"]]])

;; ===================================== Button loading sate =====================================

;; (defonce timer (reagent/atom (js/Date.)))

;; (defonce time-updater (js/setInterval
;;                        #(reset! timer (js/Date.)) 1000))


(defn button-loading-state []
  [:div
   [:div.page-header [:h3 [:b "Button-loading-state not-working"]]]
   [button {:bs-style "primary"
            ;; :disabled
            ;; :on-click
            } "loading button"]])

;; ======================================= Button Groups =========================================

(def button-group (reagent/adapt-react-class (aget js/ReactBootstrap "ButtonGroup")))

(defn button-groups []
  [:div
   [:div.page-header [:h3 [:b "Button-groups"]]]
   [button-group
    [button "Left"]
    [button "middle"]
    [button "right"]]])

;; ======================================= Button-toolbar-group ===================================

(defn button-toolbar-group []
  [:div
   [:div.page-header [:h3 [:b "Button-toolbar-group"]]]
   [button-tool-bar
    [button-group
     [button "Left"]
     [button "middle"]
     [button "right"]]]
   ])

;; ======================================= left-tabs ===================================

(def tab (reagent/adapt-react-class (aget js/ReactBootstrap "Tab")))

(def tabs (reagent/adapt-react-class (aget js/ReactBootstrap "Tabs")))

(defn left-tabs []
  [:div
   [:div.page-header [:h3 [:b "Left Tabs"]]]
   [tabs {:default-active-key 3 :position "left" :tab-width 3}
    [tab {:event-key (js* "{1}") :title "Tab 1"} "Tab 1 contant"]
    [tab {:event-key 2 :title "Tab 2"} "Tab 2 contant"]
    [tab {:event-key 3 :title "Tab 3"} "Tab 3 contant"]]])







(defn home []
  [:div.container
   [:div.page-header
    [:h2 "React-BootStrap"]]
   [buttons]
   [buttons-size]
   [block-level-buttons]
   [active-buttons]
   [disabled-buttons]
   [button-tag]
   [button-loading-state]
   [button-groups]
   [left-tabs]])

(defn render-sample []
  (reagent/render-component [home]
                            (.getElementById js/document "app")))
(render-sample)
