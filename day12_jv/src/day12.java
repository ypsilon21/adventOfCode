import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;

public class day12 {
    public static int width;
    public static int height;

    public static void main(String[] args) throws IOException {
        char[][] matrix = readFile();

        for(char[] l : matrix){
            for(char c : l){
                System.out.print(c);
            }
            System.out.print("\n");
        }
        LinkedList<Garden> gardens = getGardens(matrix);
        width = matrix[0].length;
        height = matrix.length;
        for(Garden g : gardens){
            g.print();
        }
        for(int i = 0; i < gardens.size(); i++){
            Garden garden = gardens.pop();
            gardens.addAll(garden.splitGarden());
        }

        int[] res = getCosts(gardens);
        System.out.println("First result:  " + res[0]);
        System.out.println("Second result: " + res[1]);
    }

    private static char[][] readFile() throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader("src/input.txt"));
        LinkedList<char[]> lines = new LinkedList<>();
        String line;
        while((line = fileReader.readLine()) != null){
            lines.add(line.toCharArray());
        }
        char[][] res = new char[lines.size()][lines.getFirst().length];
        int i = 0;
        for(char[] l : lines) {
            res[i] = l;
            i++;
        }
        return res;
    }

    private static LinkedList<Garden> getGardens(char[][] matrix){
        LinkedList<Garden> gardens = new LinkedList<>();
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                char id = matrix[i][j];
                Garden garden = null;
                for(Garden g : gardens){
                    if(g.id == id){
                        garden = g;
                        break;
                    }
                }
                if(garden == null){
                    garden = new Garden(id);
                    gardens.add(garden);
                }
                garden.addField(new int[]{i,j});
            }
        }
        return gardens;
    }

    private static int[] getCosts(LinkedList<Garden> gardens){
        int[] res = new int[2];
        for(Garden garden : gardens){
            res[0] += garden.getArea() * garden.getCircumference();
            res[1] += garden.getArea() * garden.getSidesCircumference();
            //System.out.println(garden.getArea() + ", " + garden.getCircumference());
        }
        return res;
    }
}