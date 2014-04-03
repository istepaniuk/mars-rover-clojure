(ns rover.core-test
  (:require [clojure.test :refer :all]
            [rover.core :refer :all]))

(deftest the-mars-rover
  (testing "stays in the same position if it gets no commands"
    (is (= {:x 0 :y 0 :heading :north} (move-rover {:x 0 :y 0 :heading :north} "" []))))
  (testing "moves forward"
    (is (= {:x 0 :y 1 :heading :north} (move-rover {:x 0 :y 0 :heading :north} "F" []))))
  (testing "moves backwards"
    (is (= {:x 0 :y 1 :heading :north} (move-rover {:x 0 :y 2 :heading :north} "B" []))))
  (testing "moves forward twice"
    (is (= {:x 0 :y 2 :heading :north} (move-rover {:x 0 :y 0 :heading :north} "FF" []))))
  (testing "rotates right"
    (is (= {:x 0 :y 0 :heading :east} (move-rover {:x 0 :y 0 :heading :north} "R" []))))
  (testing "rotates right twice"
    (is (= {:x 0 :y 0 :heading :south} (move-rover {:x 0 :y 0 :heading :north} "RR" []))))
  (testing "rotates left"
    (is (= {:x 0 :y 0 :heading :west} (move-rover {:x 0 :y 0 :heading :north} "L"[]))))
  (testing "moves forward while heading east"
    (is (= {:x 1 :y 0 :heading :east} (move-rover {:x 0 :y 0 :heading :east} "F" []))))
  (testing "The position wraps arround 100 because the planet is round"
    (is (= {:x 1 :y 99 :heading :east} (move-rover {:x 99 :y 1 :heading :south} "FFLFF" []))))
  (testing "stops moving when it encounters an obstacle"
    (is (= {:x 0 :y 2 :heading :north} (move-rover {:x 0 :y 0 :heading :north} "FFFF" [{:x 0 :y 3}])))))
