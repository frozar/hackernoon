(ns core)

(defn initial-application-state []
  {
   :bubbles [{:id "root"}]
   :links []
   })

(defonce appstate
  (atom (initial-application-state)))

(defn- add-bubble [appstate bubble]
  (update appstate :bubbles conj bubble))

(defn add-bubble! [bubble]
  (swap! appstate (fn [appstate_arg] (add-bubble appstate_arg bubble))))

(defmacro BANG
  "Define the side-effect version of a given function 'func-name'"
  [func-name]
  (let [func-name-banged (symbol (str func-name "!"))]
    `(defn ~func-name-banged [~'arg]
       (swap! appstate (fn [~'appstate_arg] (~func-name ~'appstate_arg ~'arg))))))

(macroexpand-1 '(BANG add-bubble))
;; => (clojure.core/defn add-bubble! [arg]
;;      (clojure.core/swap! core/appstate
;;        (clojure.core/fn [appstate_arg]
;;          (add-bubble appstate_arg arg))))

(macroexpand '(BANG add-bubble))
;; => (def add-bubble!
;;      (clojure.core/fn
;;        ([arg]
;;          (clojure.core/swap! core/appstate
;;            (clojure.core/fn [appstate_arg]
;;              (add-bubble appstate_arg arg))))))

(symbol (str "add-bubble" "!"))
;; => add-bubble!
(type (symbol (str "add-bubble" "!")))
;; => clojure.lang.Symbol
(name (symbol (str "add-bubble" "!")))
;; => "add-bubble!"
(type (name (symbol (str "add-bubble" "!"))))
;; => java.lang.String

(macroexpand `arg)
;; => core/arg
(macroexpand `'arg)
;; => (quote core/arg)
(macroexpand `~'arg)
;; => arg

(type add-bubble)
;; => core$add_bubble
(type (var add-bubble))
;; => clojure.lang.Var
(meta (var add-bubble))
;; => {:private true,
;;     :arglists ([appstate bubble]),
;;     :line 12,
;;     :column 1,
;;     :file ".../clojurescript macro not so long journey/part2/appstate/src/appstate.clj",
;;     :name add-bubble,
;;     :ns #namespace[core]}

(-> add-bubble var meta :arglists first rest)
;; => (bubble)

(defmacro BANG
  "Define the side-effect version of a given function 'func-name'"
  [func-name]
  (let [func-name-banged (symbol (str func-name "!"))
        func-var func-name
        ;; arg-list (-> func-var meta :arglists first rest)
        ]
    (type func-var)
    ;; `(defn ~func-name-banged ~@arg-list
    ;;    (swap! appstate (fn [~'appstate_arg] (~func-name ~'appstate_arg ~@arg-list))))
    ))

(macroexpand-1 '(BANG add-bubble))

(def arg-example 42)

(defn f-example [arg]
  (type arg))

(f-example arg-example)
;; => java.lang.Long

(defmacro m-example [arg]
  (type arg))

(m-example arg-example)
;; => clojure.lang.Symbol
