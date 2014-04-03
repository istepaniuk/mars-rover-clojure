(ns rover.core-test
  (:require [clojure.test :refer :all]
            [rover.core :refer :all]))

(defn- calculate-rotation[command]
  (get {"R" 1 "L" -1} command 0)
  )

(defn- calculate-new-bearing [old-bearing command]
  (let [all-bearings [:north :east :south :west]]
    (let [new-index (+ (calculate-rotation command) (.indexOf all-bearings old-bearing))]
      (get all-bearings (mod new-index (count all-bearings))
        )
      )
    )
  )

(def ^:private forward-displacements {:north {:delta-x 0  :delta-y 1 }
                                      :east  {:delta-x 1  :delta-y 0 }
                                      :south {:delta-x 0  :delta-y -1}
                                      :west  {:delta-x -1 :delta-y 0 }})

(defn- backward-displacements [bearing]
  (let [{delta-x :delta-x delta-y :delta-y} (forward-displacements bearing)]
    {:delta-x (- delta-x) :delta-y (- delta-y)})
  )

(defn- calculate-displacement [bearing command]
  (get { "F" (forward-displacements bearing)
         "B" (backward-displacements bearing)
         } command {:delta-x 0 :delta-y 0})
  )

(defn- do-command [position command]
  (let [{x :x y :y bearing :bearing} position]
    (let [{delta-x :delta-x delta-y :delta-y} (calculate-displacement bearing command)]
      {:x (+ x delta-x) :y (+ y delta-y) :bearing (calculate-new-bearing bearing command)}
      )
    )
  )

(defn move-rover [position commands]
  (reduce do-command position (map str commands))
  )

(deftest mars-rover-movements
  (testing "The rover stays in the same position if it gets no commands"
    (is (= {:x 0 :y 0 :bearing :north} (move-rover {:x 0 :y 0 :bearing :north} ""))))
  (testing "The rover moves forward"
    (is (= {:x 0 :y 1 :bearing :north} (move-rover {:x 0 :y 0 :bearing :north} "F"))))
  (testing "The rover moves backwards"
    (is (= {:x 0 :y -1 :bearing :north} (move-rover {:x 0 :y 0 :bearing :north} "B"))))
  (testing "The rover moves forward twice"
    (is (= {:x 0 :y 2 :bearing :north} (move-rover {:x 0 :y 0 :bearing :north} "FF"))))
  (testing "The rover rotates right"
    (is (= {:x 0 :y 0 :bearing :east} (move-rover {:x 0 :y 0 :bearing :north} "R"))))
  (testing "The rover rotates right twice"
    (is (= {:x 0 :y 0 :bearing :south} (move-rover {:x 0 :y 0 :bearing :north} "RR"))))
  (testing "The rover rotates left"
    (is (= {:x 0 :y 0 :bearing :west} (move-rover {:x 0 :y 0 :bearing :north} "L"))))
  (testing "The rover moves forward while heading east"
    (is (= {:x 1 :y 0 :bearing :east} (move-rover {:x 0 :y 0 :bearing :east} "F")))))