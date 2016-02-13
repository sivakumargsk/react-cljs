(ns react-cljs.core
  (:require-macros [secretary.core :refer [defroute]])
  (:require [goog.events :as events]
            [goog.dom :as dom]
            [goog.history.EventType :as EventType]
            [reagent.core :as r])
  (:import goog.History))


;; ------------------------------
;; reagent 0.5.0

(defonce person (r/atom {:name {:first-name "John" :last-name "Smith"}}))

(defn input [label val]
  [:div
   label
   [:input {:value @val
            :on-change #(reset! val (-> % .-target .-value))}]])


;; --------------------------------------
;; r/wrap function

(defn name-edit [n]
  (let [{:keys [first-name last-name]} @n]
    [:div
     [:p "My name is" first-name " " last-name "."]
     [input "First name: "(r/wrap first-name swap! n assoc :first-name)]
     [input "last name: " (r/wrap last-name swap! n assoc :last-name)]]))

(defn parent []
  [:div
   [:p "Current state: " (pr-str @person)]
   [name-edit (r/wrap (:name @person) swap! person assoc :name)]])


;; --------------------------------------
;; r/cursor

(defn cursor-name-edit [n]
  (let [{:keys [first-name last-name]} @n]
    [:div
     [:p "My name is" first-name " " last-name "."]
     [input "First name: "(r/cursor n [:first-name])]
     [input "last name: " (r/cursor n [:last-name])]]))

(defn cursor-parent []
  [:div
   [:p "Current state: " (pr-str @person)]
   [name-edit (r/cursor person [:name])]])

;; Cursors can now also be generalized to use any transformation of data from and to a
;; source atom (or many atoms, for that matter). To use that, you pass a function to
;; cursor instead of an atom, as in this example:

(defn person-get-set
  ([k] (get-in @person k))
  ([k v] (swap! person assoc-in k v)))

(defn get-set-parent []
  [:div
   [:p "Current state: " (pr-str @person)]
   [cursor-name-edit (r/cursor person-get-set [:name])]])


;; ---------------------------------------
;; Simple react-integration


;; ------ low level ----------------------

(defn integration []
  [:div
   [:div.foo "Hello " [:strong "world"]]

  (r/create-element "div"
                    #js{:className "foo"}
                    "Hello "
                    (r/create-element "strong"
                                      #js{}
                                      "world"))
  (r/create-element "div"
                    #js{:className "foo"}
                    "Hello "
                    (r/as-element [:strong "world"]))
  [:div.foo "Hello " (r/create-element "strong"
                                      #js{}
                                      "world")]])

;; ------------------------------------
;; the new function adapt-react-class, that will take any React class,
;; and turn it into something that can be called from Reagent directly.
;; The example from above would then become:

;; (React -----> Reagent)

(def div-adapter (r/adapt-react-class "div"))

(defn adapted []
  [div-adapter {:class "foo"}
   "Hello " [:strong "world"]])


;; (reagent -------> React)

(defn exported [props]
  [:div "Hi, " (:name props)])

(def react-comp (r/reactify-component exported))

(defn could-be-jsx []
  (r/create-element react-comp #js{:name "world"}))



(defn render-sample []
  (r/render-component [adapted]
                            (.getElementById js/document "app")))

(render-sample)
