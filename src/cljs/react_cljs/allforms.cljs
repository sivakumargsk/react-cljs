(ns react-cljs.allforms
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [accountant.core :as accountant]))



;; -------------------------
;; input-elements for all records

(defn input-element [id ttype data-set placeholder in-focus]
  [:input.form-control {:id id
                        :type ttype
                        :value (@data-set id)
                        :placeholder placeholder
                        :on-change #(swap! data-set assoc id (-> % .-target .-value))
                        :on-blur  #(reset! in-focus "on")
                        }])


;; -------------------------
;; masavi Data-set

(def masavi-dataset (atom [{:id 1
                            :s-no 101
                            :village-name "pooram"
                            :title "house-sales"
                            :tehsil "Appa Rao"
                            :year 2008
                            :rack-location "A"}
                           {:id 2
                            :s-no 102
                            :village-name "ramapuram"
                            :title "house-tax"
                            :tehsil "Rama Rao"
                            :year 2012
                            :rack-location "B"}]))


(defn masavi-table [all-documents]
  [:div.container
   [:button.btn.btn-primary {:on-click #(accountant/navigate! "/masavi/add")} "add-new"]
   [:div.box-body
    [:table.table.table-striped
     [:thead
      [:tr
       [:th "S.No"]
       [:th "Village Name"]
       [:th "Title"]
       [:th "Tehsil"]
       [:th "Year"]
       [:th "Rack Location"]
       [:th "Update"]
       [:th "Delete"]]]
     [:tbody
      (for [doc all-documents]
        ^{:key (doc :id)}
        [:tr
         [:td (doc :s-no)]
         [:td (doc :village-name)]
         [:td (doc :title)]
         [:td (doc :tehsil)]
         [:td (doc :year)]
         [:td (doc :rack-location)]
         [:td  [:button.btn.btn-success.btn-sm.glyphicon.glyphicon-edit
                {:on-click #(accountant/navigate! "/masavi/update")}]]
         [:td  [:button.btn.btn-danger.btn-sm.glyphicon.glyphicon-remove]]])]]]])

(defn masavi-validator [data-set]
  (first (b/validate data-set
                     :id  [[v/required :message "Filed is required"]]
                     :s-no [[v/required :message "Filed is required"]]
                     :village-name [[v/required :message "Filed is required"]]
                     :title [[v/required :message "Filed is required"]]
                     :tehsil [[v/required :message "Filed is required"]]
                     :year [[v/required :message "Filed is required"]]
                     :rack-location [[v/required :message "Filed is required"]])))

(defn masavi-row [id label ttype data-set focus]
  (let [input-focus (atom nil)]
    (fn []
      [:div.form-group
       [:div.col-md-12
        [:div.row
         [:div.col-md-2 [:label label]]
         [:div.col-md-7 [input-element id ttype data-set label input-focus]]
         [:div.col-md-3 (if (or @input-focus @focus)
                          (if (= nil (masavi-validator @data-set))
                            [:div]
                            [:div {:style  {:color "red"}}
                             [:b (str (first ((masavi-validator @data-set) id)))]])
                          [:div])]]]] )))


(defn masavi-save-onclick [data-set focus]
  (if (= nil (masavi-validator @data-set))
    (do
      (swap! data-set conj @data-set)
      (accountant/navigate! "/"))
    (reset! focus "on")))


(defn masavi-document-template [doc-name data-set focus fun ]
  [:div.container
   [:div.panel.panel-primary.model-dialog
    [:div.panel-heading
     [:h2 doc-name]]
    [:div.panel-boby
     [:div.container
      [:div.form-group
       [masavi-row :id "Id" "number" data-set focus]
       [masavi-row :s-no "S.No" "text" data-set focus]
       [masavi-row :village-name "Village Name" "text" data-set focus ]
       [masavi-row :title "Title" "text" data-set focus]
       [masavi-row :tehsil "Tehsil" "text" data-set focus]
       [masavi-row :year "Year" "text" data-set focus]
       [masavi-row :rack-location "Rack Location" "text" data-set focus]
       [:button.btn.btn-primary {:on-click fun} "Save" ]
       [:button.btn.btn-primary {:on-click #(accountant/navigate! "/")} "Cancel"]]]]]])


(defn masavi-add-record []
  (let [add-data (atom {})
        focus (atom nil)]
    (fn []
      [masavi-document-template
       "New Document"
       add-data focus
       #(masavi-save-onclick add-data focus)])))

(defn masavi-upd-record [id dmt]
  (let [update-data (atom {})
        focus (atom nil)]
    (fn []
      [masavi-document-template
       "Update Document"
       update-data focus
       #(masavi-save-onclick update-data focus)])))

(defn current-page []
  [:div [(session/get :current-page)]])

(secretary/defroute "/masavi/add" []
  (session/put! :current-page #'masavi-add-record))

(secretary/defroute "/masavi/update" []
  (session/put! :current-page #'masavi-upd-record))


;; --------------------------------
;; Consolidation form Record

(def consolidation-dataset (atom [{:id 1
                                   :s-no 101
                                   :village-name "pooram"
                                   :title "house-sales"
                                   :tehsil "Appa Rao"
                                   :year 2008
                                   :rack-location "A"}
                                  {:id 2
                                   :s-no 102
                                   :village-name "ramapuram"
                                   :title "house-tax"
                                   :tehsil "Rama Rao"
                                   :year 2012
                                   :rack-location "B"}]))


(defn consolidation-table [all-documents]
  [:div.container
   [:button.btn.btn-primary {:on-click #(accountant/navigate! "/consolidation/add")} "add-new"]
   [:div.box-body
    [:table.table.table-striped
     [:thead
      [:tr
       [:th "S.No"]
       [:th "Village Name"]
       [:th "Title"]
       [:th "Tehsil"]
       [:th "Year"]
       [:th "Rack Location"]
       [:th "Update"]
       [:th "Delete"]]]
     [:tbody
      (for [doc all-documents]
        ^{:key (doc :id)}
        [:tr
         [:td (doc :s-no)]
         [:td (doc :village-name)]
         [:td (doc :title)]
         [:td (doc :tehsil)]
         [:td (doc :year)]
         [:td (doc :rack-location)]
         [:td  [:button.btn.btn-success.btn-sm.glyphicon.glyphicon-edit
                {:on-click #(accountant/navigate! "/consolidation/update")}]]
         [:td  [:button.btn.btn-danger.btn-sm.glyphicon.glyphicon-remove]]])]]]])

(defn consolidation-validator [data-set]
  (first (b/validate data-set
                     :id  [[v/required :message "Filed is required"]]
                     :s-no [[v/required :message "Filed is required"]]
                     :village-name [[v/required :message "Filed is required"]]
                     :title [[v/required :message "Filed is required"]]
                     :tehsil [[v/required :message "Filed is required"]]
                     :year [[v/required :message "Filed is required"]]
                     :rack-location [[v/required :message "Filed is required"]])))

(defn consolidation-row [id label ttype data-set focus]
  (let [input-focus (atom nil)]
    (fn []
      [:div.form-group
       [:div.col-md-12
        [:div.row
         [:div.col-md-2 [:label label]]
         [:div.col-md-7 [input-element id ttype data-set label input-focus]]
         [:div.col-md-3 (if (or @input-focus @focus)
                          (if (= nil (consolidation-validator @data-set))
                            [:div]
                            [:div {:style  {:color "red"}}
                             [:b (str (first ((consolidation-validator @data-set) id)))]])
                          [:div])]]]] )))


(defn consolidation-save-onclick [data-set focus]
  (if (= nil (consolidation-validator @data-set))
    (do
      (swap! data-set conj @data-set)
      (accountant/navigate! "/"))
    (reset! focus "on")))


(defn consolidation-document-template [doc-name data-set focus fun ]
  [:div.container
   [:div.panel.panel-primary.model-dialog
    [:div.panel-heading
     [:h2 doc-name]]
    [:div.panel-boby
     [:div.container
      [:div.form-group
       [consolidation-row :id "Id" "number" data-set focus]
       [consolidation-row :s-no "S.No" "text" data-set focus]
       [consolidation-row :village-name "Village Name" "text" data-set focus ]
       [consolidation-row :title "Title" "text" data-set focus]
       [consolidation-row :tehsil "Tehsil" "text" data-set focus]
       [consolidation-row :year "Year" "text" data-set focus]
       [consolidation-row :rack-location "Rack Location" "text" data-set focus]
       [:button.btn.btn-primary {:on-click fun} "Save" ]
       [:button.btn.btn-primary {:on-click #(accountant/navigate! "/")} "Cancel"]]]]]])


(defn consolidation-add-record []
  (let [add-data (atom {})
        focus (atom nil)]
    (fn []
      [consolidation-document-template
       "New Document"
       add-data focus
       #(consolidation-save-onclick add-data focus)])))

(defn consolidation-upd-record [id dmt]
  (let [update-data (atom {})
        focus (atom nil)]
    (fn []
      [consolidation-document-template
       "Update Document"
       update-data focus
       #(consolidation-save-onclick update-data focus)])))

;; (defn current-page []
;;   [:div [(session/get :current-page)]])

(secretary/defroute "/consolidation/add" []
  (session/put! :current-page #'consolidation-add-record))


(secretary/defroute "/consolidation/update" []
  (session/put! :current-page #'consolidation-upd-record))
