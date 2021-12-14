/*
--- Day 13: Transparent Origami ---

You reach another volcanically active part of the cave. It would be nice if you could do some kind of thermal imaging so you could tell ahead of time which caves are too hot to safely enter.

Fortunately, the submarine seems to be equipped with a thermal camera! When you activate it, you are greeted with:

Congratulations on your purchase! To activate this infrared thermal imaging
camera system, please enter the code found on page 1 of the manual.

Apparently, the Elves have never used this feature. To your surprise, you manage to find the manual; as you go to open it, page 1 falls out. It's a large sheet of transparent paper! The transparent paper is marked with random dots and includes instructions on how to fold it up (your puzzle input). For example:

6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0

fold along y=7
fold along x=5

The first section is a list of dots on the transparent paper. 0,0 represents the top-left coordinate. The first value, x, increases to the right. The second value, y, increases downward. So, the coordinate 3,0 is to the right of 0,0, and the coordinate 0,7 is below 0,0. The coordinates in this example form the following pattern, where # is a dot on the paper and . is an empty, unmarked position:

...#..#..#.
....#......
...........
#..........
...#....#.#
...........
...........
...........
...........
...........
.#....#.##.
....#......
......#...#
#..........
#.#........

Then, there is a list of fold instructions. Each instruction indicates a line on the transparent paper and wants you to fold the paper up (for horizontal y=... lines) or left (for vertical x=... lines). In this example, the first fold instruction is fold along y=7, which designates the line formed by all of the positions where y is 7 (marked here with -):

...#..#..#.
....#......
...........
#..........
...#....#.#
...........
...........
-----------
...........
...........
.#....#.##.
....#......
......#...#
#..........
#.#........

Because this is a horizontal line, fold the bottom half up. Some of the dots might end up overlapping after the fold is complete, but dots will never appear exactly on a fold line. The result of doing this fold looks like this:

#.##..#..#.
#...#......
......#...#
#...#......
.#.#..#.###
...........
...........

Now, only 17 dots are visible.

Notice, for example, the two dots in the bottom left corner before the transparent paper is folded; after the fold is complete, those dots appear in the top left corner (at 0,0 and 0,1). Because the paper is transparent, the dot just below them in the result (at 0,3) remains visible, as it can be seen through the transparent paper.

Also notice that some dots can end up overlapping; in this case, the dots merge together and become a single dot.

The second fold instruction is fold along x=5, which indicates this line:

#.##.|#..#.
#...#|.....
.....|#...#
#...#|.....
.#.#.|#.###
.....|.....
.....|.....

Because this is a vertical line, fold left:

#####
#...#
#...#
#...#
#####
.....
.....

The instructions made a square!

The transparent paper is pretty big, so for now, focus on just completing the first fold. After the first fold in the example above, 17 dots are visible - dots that end up overlapping after the fold is completed count as a single dot.

How many dots are visible after completing just the first fold instruction on your transparent paper?

Your puzzle answer was 788.
--- Part Two ---

Finish folding the transparent paper according to the instructions. The manual says the code is always eight capital letters.

What code do you use to activate the infrared thermal imaging camera system?

Your puzzle answer was KJBKEUBG.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day13 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/inputs/Day13.txt"));
        List<String> input = new ArrayList<>();
        while (scanner.hasNextLine()) {
            input.add(scanner.nextLine());
        }
        scanner.close();

        int indexOfBreak = input.indexOf("");

        List<int[]> dots = input.subList(0, indexOfBreak).stream().map(line -> Arrays.stream(line.split(",")).map(Integer::parseInt).mapToInt(Integer::intValue).toArray()).collect(Collectors.toList());
        List<String[]> instructions = input.subList(indexOfBreak + 1, input.size()).stream().map(line -> line.split(" ")[2].split("=")).collect(Collectors.toList());

        int maxX = 0;
        int maxY = 0;
        for (int[] pair : dots) {
            if (pair[0] > maxX) {
                maxX = pair[0];
            }
            if (pair[1] > maxY) {
                maxY = pair[1];
            }
        }

        List<List<Character>> grid = new ArrayList<>();
        for (int i = 0; i < maxY + 1; i++) {
            grid.add(new ArrayList<>());
            for (int j = 0; j < maxX + 1; j++) {
                grid.get(i).add(' ');
            }
        }

        for (int[] pair : dots) {
            grid.get(pair[1]).set(pair[0], '*');
        }

        // Pt. 1
//        for (String[] instruction : instructions.subList(0, 1)) {
        // Pt. 2
        for (String[] instruction : instructions) {

            int subtractionFactor = 2;

            if (instruction[0].equals("x")) {
                for (int i = 0; i < grid.size(); i++) {
                    for (int j = Integer.parseInt(instruction[1]) + 1; j < grid.get(0).size(); j++) {
                        if (grid.get(i).get(j) == '*') {
                            grid.get(i).set(j - subtractionFactor, '*');
                        }
                        subtractionFactor += 2;
                    }
                    subtractionFactor = 2;
                }
                grid = grid.stream().map(row -> row.subList(0, Integer.parseInt(instruction[1]))).collect(Collectors.toList());
            }

            if (instruction[0].equals("y")) {
                for (int i = Integer.parseInt(instruction[1]) + 1; i < grid.size(); i++) {
                    for (int j = 0; j < grid.get(0).size(); j++) {
                        if (grid.get(i).get(j) == '*') {
                            grid.get(i - subtractionFactor).set(j, '*');
                        }
                    }
                    subtractionFactor += 2;
                }
                grid = grid.subList(0, Integer.parseInt(instruction[1]));
            }
        }

        // Pt. 1 only
//        int total = 0;
//        for (List<Character> row : grid) {
//            for (Character col : row) {
//                if (col == '*') {
//                    total++;
//                }
//            }
//        }
//        System.out.println("Answer1: " + total);

        System.out.println("Answer 2:");
        for (List<Character> row : grid) {
            System.out.println(row.stream().map(String::valueOf).collect(Collectors.joining()));
        }
    }
}
