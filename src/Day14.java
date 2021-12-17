/*
--- Day 14: Extended Polymerization ---

The incredible pressures at this depth are starting to put a strain on your submarine. The submarine has polymerization equipment that would produce suitable materials to reinforce the submarine, and the nearby volcanically-active caves should even have the necessary input elements in sufficient quantities.

The submarine manual contains instructions for finding the optimal polymer formula; specifically, it offers a polymer template and a list of pair insertion rules (your puzzle input). You just need to work out what polymer would result after repeating the pair insertion process a few times.

For example:

NNCB

CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C

The first line is the polymer template - this is the starting point of the process.

The following section defines the pair insertion rules. A rule like AB -> C means that when elements A and B are immediately adjacent, element C should be inserted between them. These insertions all happen simultaneously.

So, starting with the polymer template NNCB, the first step simultaneously considers all three pairs:

    The first pair (NN) matches the rule NN -> C, so element C is inserted between the first N and the second N.
    The second pair (NC) matches the rule NC -> B, so element B is inserted between the N and the C.
    The third pair (CB) matches the rule CB -> H, so element H is inserted between the C and the B.

Note that these pairs overlap: the second element of one pair is the first element of the next pair. Also, because all pairs are considered simultaneously, inserted elements are not considered to be part of a pair until the next step.

After the first step of this process, the polymer becomes NCNBCHB.

Here are the results of a few steps using the above rules:

Template:     NNCB
After step 1: NCNBCHB
After step 2: NBCCNBBBCBHCB
After step 3: NBBBCNCCNBBNBNBBCHBHHBCHB
After step 4: NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB

This polymer grows quickly. After step 5, it has length 97; After step 10, it has length 3073. After step 10, B occurs 1749 times, C occurs 298 times, H occurs 161 times, and N occurs 865 times; taking the quantity of the most common element (B, 1749) and subtracting the quantity of the least common element (H, 161) produces 1749 - 161 = 1588.

Apply 10 steps of pair insertion to the polymer template and find the most and least common elements in the result. What do you get if you take the quantity of the most common element and subtract the quantity of the least common element?

Your puzzle answer was 2797.
--- Part Two ---

The resulting polymer isn't nearly strong enough to reinforce the submarine. You'll need to run more steps of the pair insertion process; a total of 40 steps should do it.

In the above example, the most common element is B (occurring 2192039569602 times) and the least common element is H (occurring 3849876073 times); subtracting these produces 2188189693529.

Apply 40 steps of pair insertion to the polymer template and find the most and least common elements in the result. What do you get if you take the quantity of the most common element and subtract the quantity of the least common element?

Your puzzle answer was 2926813379532.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day14 {
    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        Scanner scanner = new Scanner(new File("src/inputs/Day14.txt"));
        List<String> input = new ArrayList<>();
        while (scanner.hasNextLine()) {
            input.add(scanner.nextLine());
        }
        scanner.close();

        // Pt. 1, iterations = 10
        // Pt. 2, iterations = 40
        int iterations = 40;

        String template = input.get(0);
        List<String> rawPairs = input.subList(2, input.size());

        Map<String, String> pairsMap = new HashMap<>();

        for (String pair : rawPairs) {
            String[] splitPair = pair.split(" -> ");
            pairsMap.put(splitPair[0], splitPair[1]);
        }

        Map<String, Long> freqMap = new HashMap<>();
        Map<String, Long> freqBin = new HashMap<>();

        for (int i = 0; i < template.length() - 1; i++) {
            String sub = template.substring(i, i + 2);
            if (freqMap.containsKey(sub)) {
                freqMap.put(sub, freqMap.get(sub) + 1);
            } else {
                freqMap.put(sub, 1L);
            }
        }


        for (int i = 0; i < iterations - 1; i++) {
            for (Map.Entry<String, Long> entry : freqMap.entrySet()) {
                String oldPair = entry.getKey();
                String child = pairsMap.get(oldPair);
                String newPair1 = oldPair.charAt(0) + child;
                String newPair2 = child + oldPair.charAt(1);
                if (freqBin.containsKey(newPair1)) {
                    freqBin.put(newPair1, freqBin.get(newPair1) + freqMap.get(oldPair));
                } else {
                    freqBin.put(newPair1, freqMap.get(oldPair));
                }
                if (freqBin.containsKey(newPair2)) {
                    freqBin.put(newPair2, freqBin.get(newPair2) + freqMap.get(oldPair));
                } else {
                    freqBin.put(newPair2, freqMap.get(oldPair));
                }
            }
            freqMap = new HashMap<>(freqBin);
            freqBin = new HashMap<>();
        }

        for (Map.Entry<String, Long> entry : freqMap.entrySet()) {
            String oldPair = entry.getKey();
            String child = pairsMap.get(oldPair);
            String char1 = oldPair.substring(0, 1);
            if (freqBin.containsKey(child)) {
                freqBin.put(child, freqBin.get(child) + freqMap.get(oldPair));
            } else {
                freqBin.put(child, freqMap.get(oldPair));
            }
            if (freqBin.containsKey(char1)) {
                freqBin.put(char1, freqBin.get(char1) + freqMap.get(oldPair));
            } else {
                freqBin.put(char1, freqMap.get(oldPair));
            }
        }
        String lastChar = template.substring(template.length() - 1);
        freqBin.put(lastChar, freqBin.get(lastChar) + 1);

        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        for (long value : freqBin.values()) {
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }
        System.out.println("Answer: " + (max - min));

        long end = System.currentTimeMillis();
        System.out.println("Execution time: " + (end - start));
    }
}
