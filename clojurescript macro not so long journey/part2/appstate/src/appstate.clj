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
  (let [func-name-banged (symbol (str func-name "!"))
        arg-symbol (gensym)
        appstate-arg-symbol (gensym)]
    `(defn ~func-name-banged [~arg-symbol]
       (swap! appstate (fn [~appstate-arg-symbol] (~func-name ~appstate-arg-symbol ~arg-symbol))))))

(macroexpand-1 '(BANG add-bubble))
;; => (clojure.core/defn add-bubble! [G__7657]
;;      (clojure.core/swap! core/appstate
;;        (clojure.core/fn [G__7658]
;;          (add-bubble G__7658 G__7657))))
;; => (clojure.core/defn add-bubble! [arg]
;;      (clojure.core/swap! core/appstate
;;        (clojure.core/fn [appstate-arg]
;;          (add-bubble appstate-arg arg))))
;; => (clojure.core/defn add-bubble! [arg] (clojure.core/swap! core/appstate (clojure.core/fn [appstate_arg] (add-bubble appstate_arg arg))))
;; => (clojure.core/defn add-bubble! [core/arg-symbol] (clojure.core/swap! core/appstate (clojure.core/fn [appstate_arg] (add-bubble appstate_arg core/arg-symbol))))
;; => (clojure.core/defn add-bubble! [arg] (clojure.core/swap! core/appstate (clojure.core/fn [appstate_arg] (add-bubble appstate_arg arg))))
;; => (clojure.core/defn add-bubble! [arg]
;;      (clojure.core/swap! core/appstate
;;        (clojure.core/fn [appstate_arg]
;;          (add-bubble appstate_arg arg))))

(macroexpand '(BANG add-bubble))
;; => (def add-bubble!
;;      (clojure.core/fn
;;        ([arg]
;;          (clojure.core/swap! core/appstate
;;            (clojure.core/fn [appstate-arg]
;;              (add-bubble appstate-arg arg))))))

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


(macroexpand-1 '(BANG add-bubble))

(def dummy-arg 42)

(defn fn1 [arg]
  (type arg))

(fn1 dummy-arg)
;; => java.lang.Long

(defmacro macro1 [arg]
  (type arg))

(macro1 dummy-arg)
;; => clojure.lang.Symbol

(defn fn2 [arg]
  (meta (resolve (symbol (name arg)))))

(macroexpand '(defn fn2 [arg]
                (meta (resolve (symbol (name arg))))))
;; (def
;;   fn2
;;   (clojure.core/fn ([arg] (meta (resolve (symbol (name arg)))))))

(fn2 dummy-arg)
;; => java.lang.Long

(defmacro dummy-m [arg]
  (resolve arg))

(macroexpand '(defmacro dummy-m [arg]
                (resolve arg)))
;; (do
;;   (clojure.core/defn dummy-m ([&form &env arg] (resolve arg)))
;;   (. #'m (setMacro))
;;   #'m)

(dummy-m dummy-arg)
;; => #'core/dummy-arg

;; (macro2 dummy-arg)
;; ;; => clojure.lang.Symbol

(require '[clojure.repl])
(clojure.repl/doc ns-resolve)

(def x)
(meta (var x))

(defmacro dummy-m1 [arg]
  (prn &form))

(dummy-m1 (+ 3 2 doesn't-exist))

(defmacro dummy-m2 []
  (prn &env))

(dummy-m2)

;; toto

(defmacro BANG
  "Define the side-effect version of a given function 'func-name'"
  [func-name]
  (let [func-name-banged (symbol (str func-name "!"))
        appstate-arg-symbol (symbol "appstate-arg")
        func-var (resolve func-name)
        arg-list (-> func-var meta :arglists first rest)]
    `(defn ~func-name-banged [~@arg-list]
       (swap! appstate (fn [~appstate-arg-symbol] (~func-name ~appstate-arg-symbol ~@arg-list))))
    ))

(macroexpand-1 '(BANG add-bubble))
;; => (clojure.core/defn add-bubble! [bubble]
;;      (clojure.core/swap! core/appstate
;;        (clojure.core/fn [appstate-arg]
;;          (add-bubble appstate-arg bubble))))

'(+ 2 3)

(clojure.repl/doc var)
;; => var
;;    (var symbol)
;;    Special Form
;;    The symbol must resolve to a var, and the Var object
;;    itself (not its value) is returned. The reader macro #'x expands to (var x).
;;
;;    Please see http://clojure.org/special_forms#var
;;    The symbol must resolve to a var, and the Var object
;;    itself (not its value) is returned. The reader macro #'x expands to (var x).
