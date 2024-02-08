package ai_p1;

import java.io.*;
import java.util.*;

public class WaterJugMainmethod {

    public static void main(String[] args) throws IOException {
        List<Integer> capacities = new ArrayList<>();
        
//To fetch input file from the path and obtain the pitcher capacities and target quantity
        int targetQuantity;
        String filePath = "C:\\Users\\Mayur\\eclipse-workspace\\ai_p1\\src\\ai_p1\\input.txt";
        File file = new File(filePath);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String[] parts = br.readLine().split(",");
            for (String part : parts) {
                capacities.add(Integer.parseInt(part.trim()));
            }
            targetQuantity = Integer.parseInt(br.readLine().trim());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        
//Return the result after calculating the minimum number of steps
        int result = aStarMethod(capacities, targetQuantity);
        System.out.println("Minimum number of steps required: " + (result == Integer.MAX_VALUE ? -1 : result));

        
// Some sample test cases
        List<Integer> capacities1 = Arrays.asList(3, 5);
        int targetQuantity1 = 4;
        int result1 = aStarMethod(capacities1, targetQuantity1);
        System.out.println("Minimum number of steps for capacities [3, 5] and target quantity 4: " + (result1 == Integer.MAX_VALUE ? -1 : result1));

        List<Integer> capacities2 = Arrays.asList(2, 4, 6);
        int targetQuantity2 = 5;
        int result2 = aStarMethod(capacities2, targetQuantity2);
        System.out.println("Minimum number of steps for capacities [2, 4, 6] and target quantity 5: " + (result2 == Integer.MAX_VALUE ? -1 : result2));

        List<Integer> capacities3 = Arrays.asList(8, 9, 10);
        int targetQuantity3 = 17;
        int result3 = aStarMethod(capacities3, targetQuantity3);
        System.out.println("Minimum number of steps for capacities [8, 9, 10] and target quantity 17: " + (result3 == Integer.MAX_VALUE ? -1 : result3));
   
        List<Integer> capacities4 = Arrays.asList(4, 7);
        int targetQuantity4 = 6;
        int result4 = aStarMethod(capacities4, targetQuantity4);
        System.out.println("Minimum number of steps for capacities [4, 7] and target quantity 6: " + (result4 == Integer.MAX_VALUE ? -1 : result4));
   
    }


// A* search to find minimum number of steps, uses priority queue to explore stages with low costs first
    static int aStarMethod(List<Integer> capacities, int targetQuantity) {
        PriorityQueue<PitcherState> queue = new PriorityQueue<>(Comparator.comparingInt(
        		state -> state.getSteps() + heuristicMethod(targetQuantity, capacities)
        ));

// Maintains visited to ensure there no duplication in exploration 
// PitcherState to represent state of jugs/pitcher used
        Set<PitcherState> visited = new HashSet<>();
        PitcherState initialState = new PitcherState(new ArrayList<>(Collections.nCopies(capacities.size(), 0)), 0);

        queue.add(initialState);
        visited.add(initialState);

        while (!queue.isEmpty()) {
            PitcherState currentState = queue.poll();

            if (currentState.getTotal() == targetQuantity) {
                return currentState.getSteps();
            }

            for (int i = 0; i < capacities.size(); i++) {
                List<Integer> newState = new ArrayList<>(currentState.getPitchers());
                newState.set(i, capacities.get(i)); // Fill pitcher i
                addUnexploredStates(newState, currentState.getSteps() + 1, visited, queue);

                newState = new ArrayList<>(currentState.getPitchers());
                newState.set(i, 0); // Empty pitcher i
                addUnexploredStates(newState, currentState.getSteps() + 1, visited, queue);

                
// Transfer water between pitchers
                for (int j = 0; j < capacities.size(); j++) {
                    if (i != j) {
                        newState = new ArrayList<>(currentState.getPitchers());
                        int transfer = Math.min(newState.get(i), capacities.get(j) - newState.get(j));
                        newState.set(i, newState.get(i) - transfer);
                        newState.set(j, newState.get(j) + transfer);
                        addUnexploredStates(newState, currentState.getSteps() + 1, visited, queue);
                    }
                }
            }
        }

        return Integer.MAX_VALUE; // No possible solution found, e.g, 2 jugs 2 and 4, target 6
    }

//In case there is a state which is not explored, it will be added through this method
    private static void addUnexploredStates(List<Integer> newState, int newSteps, Set<PitcherState> visited, PriorityQueue<PitcherState> queue) {
        PitcherState newStateObj = new PitcherState(newState, newSteps);
        if (!visited.contains(newStateObj)) {
            queue.add(newStateObj);
            visited.add(newStateObj);
        }
    }

    
    private static int heuristicMethod(int targetQuantity, List<Integer> capacities) {
        int maxCapacity = Collections.max(capacities);
        return (int) Math.ceil((double) targetQuantity / maxCapacity);
    }
}

