/*
--- Day 9: Smoke Basin ---

These caves seem to be lava tubes. Parts are even still volcanically active; small hydrothermal vents release smoke into the caves that slowly settles like rain.

If you can model how the smoke flows through the caves, you might be able to avoid it and be that much safer. The submarine generates a heightmap of the floor of the nearby caves for you (your puzzle input).

Smoke flows to the lowest point of the area it's in. For example, consider the following heightmap:

2199943210
3987894921
9856789892
8767896789
9899965678

Each number corresponds to the height of a particular location, where 9 is the highest and 0 is the lowest a location can be.

Your first goal is to find the low points - the locations that are lower than any of its adjacent locations. Most locations have four adjacent locations (up, down, left, and right); locations on the edge or corner of the map have three or two adjacent locations, respectively. (Diagonal locations do not count as adjacent.)

In the above example, there are four low points, all highlighted: two are in the first row (a 1 and a 0), one is in the third row (a 5), and one is in the bottom row (also a 5). All other locations on the heightmap have some lower adjacent location, and so are not low points.

The risk level of a low point is 1 plus its height. In the above example, the risk levels of the low points are 2, 1, 6, and 6. The sum of the risk levels of all low points in the heightmap is therefore 15.

Find all of the low points on your heightmap. What is the sum of the risk levels of all low points on your heightmap?

Your puzzle answer was 570.
--- Part Two ---

Next, you need to find the largest basins so you know what areas are most important to avoid.

A basin is all locations that eventually flow downward to a single low point. Therefore, every low point has a basin, although some basins are very small. Locations of height 9 do not count as being in any basin, and all other locations will always be part of exactly one basin.

The size of a basin is the number of locations within the basin, including the low point. The example above has four basins.

The top-left basin, size 3:

2199943210
3987894921
9856789892
8767896789
9899965678

The top-right basin, size 9:

2199943210
3987894921
9856789892
8767896789
9899965678

The middle basin, size 14:

2199943210
3987894921
9856789892
8767896789
9899965678

The bottom-right basin, size 9:

2199943210
3987894921
9856789892
8767896789
9899965678

Find the three largest basins and multiply their sizes together. In the above example, this is 9 * 14 * 9 = 1134.

What do you get if you multiply together the sizes of the three largest basins?

Your puzzle answer was 899392.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day09 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/inputs/Day09.txt"));
        List<List<Integer>> heightMap = new ArrayList<>();
        while (scanner.hasNextLine()) {
            heightMap.add(Arrays.stream(scanner.nextLine().split("")).map(Integer::parseInt).collect(Collectors.toList()));
        }
        scanner.close();

        List<Integer> lowSpots = new ArrayList<>();

        for (int i = 0; i < heightMap.size(); i++) {
            for (int j = 0; j < heightMap.get(0).size(); j++) {
                List<Integer> neighbors = new ArrayList<>();
                if (i > 0) {
                    neighbors.add(heightMap.get(i - 1).get(j));
                }
                if (i < heightMap.size() - 1) {
                    neighbors.add(heightMap.get(i + 1).get(j));
                }
                if (j > 0) {
                    neighbors.add(heightMap.get(i).get(j - 1));
                }
                if (j < heightMap.get(0).size() - 1) {
                    neighbors.add(heightMap.get(i).get(j + 1));
                }

                int currentValue = heightMap.get(i).get(j);
                if (neighbors.stream().allMatch(num -> num > currentValue)) {
                    lowSpots.add(currentValue);
                }
            }
        }
        System.out.println("Answer1: " + (lowSpots.stream().mapToInt(Integer::intValue).sum() + lowSpots.size()));

        List<Integer> basinSizes = new ArrayList<>();

        for (int i = 0; i < heightMap.size(); i++) {
            for (int j = 0; j < heightMap.get(0).size(); j++) {
                int currentValue = heightMap.get(i).get(j);
                if (currentValue != -1 && currentValue != 9) {
                    basinSizes.add(exploreBasin(heightMap, i, j));
                }
            }
        }

        basinSizes.sort((a, b) -> b - a);

        System.out.println("Answer2: " + (basinSizes.get(0) * basinSizes.get(1) * basinSizes.get(2)));
    }

    public static int exploreBasin(List<List<Integer>> heightMap, int row, int col) {
        int basinSize = 1;
        heightMap.get(row).set(col, -1);

        if (row > 0 && heightMap.get(row - 1).get(col) != -1 && heightMap.get(row - 1).get(col) != 9) {
            basinSize += exploreBasin(heightMap, row - 1, col);
        }
        if (row < heightMap.size() - 1 && heightMap.get(row + 1).get(col) != -1 && heightMap.get(row + 1).get(col) != 9) {
            basinSize += exploreBasin(heightMap, row + 1, col);
        }
        if (col > 0 && heightMap.get(row).get(col - 1) != -1 && heightMap.get(row).get(col - 1) != 9) {
            basinSize += exploreBasin(heightMap, row, col - 1);
        }
        if (col < heightMap.get(0).size() - 1 && heightMap.get(row).get(col + 1) != -1 && heightMap.get(row).get(col + 1) != 9) {
            basinSize += exploreBasin(heightMap, row, col + 1);
        }

        return basinSize;
    }
}
