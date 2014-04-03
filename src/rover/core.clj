(ns rover.core
  (:gen-class))

(def ^:private planet-size
  {
    :around-x 100
    :around-y 100
    }
  )

(def ^:private forward-displacements
  {
    :north {:delta-x 0  :delta-y 1 }
    :east  {:delta-x 1  :delta-y 0 }
    :south {:delta-x 0  :delta-y -1}
    :west  {:delta-x -1 :delta-y 0 }
    }
  )

(defn- calculate-rotation[command]
  (get { "R" 1 "L" -1} command 0)
  )

(defn- calculate-new-heading [old-heading command]
  (let [all-headings [:north :east :south :west]]
    (let [new-index (+ (calculate-rotation command) (.indexOf all-headings old-heading))]
      (get all-headings (mod new-index (count all-headings))
           )
      )
    )
  )

(defn- backward-displacements [heading]
  (let [{delta-x :delta-x delta-y :delta-y} (forward-displacements heading)]
    {:delta-x (- delta-x) :delta-y (- delta-y)})
  )

(defn- calculate-displacement [heading command]
  (get {
         "F" (forward-displacements heading)
         "B" (backward-displacements heading)
         }
       command {:delta-x 0 :delta-y 0}
       )
  )

(defn- is-coordinate-an-obstacle? [coordinate obstacles]
  (some #(= % coordinate) obstacles)
  )

(defn- calculate-new-coordinates [position command]
  (let [{x :x y :y heading :heading} position]
    (let [{delta-x :delta-x delta-y :delta-y} (calculate-displacement heading command)]
      {:x (mod (+ x delta-x) (planet-size :around-x))
       :y (mod (+ y delta-y) (planet-size :around-y))}
      )
    )
  )

(defn move-rover [initial-position commands obstacles]
  (letfn [(do-command [position command]
    (let [{x :x y :y heading :heading} position]
      (let [{new-x :x new-y :y} (calculate-new-coordinates position command)]
        (if (is-coordinate-an-obstacle? {:x new-x :y new-y} obstacles)
          position
          {:x new-x :y new-y :heading (calculate-new-heading heading command)}
          )
        )
      )
    )]
    (reduce do-command initial-position (map str commands)
      )
    )
  )

(defn -main
  (println "Hey!"))
