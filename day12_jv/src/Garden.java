import java.util.LinkedList;

public class Garden {
    char id;
    LinkedList<int[]> fields;
    LinkedList<int[][]> edges;
    public Garden(char id){
        this.id = id;
        this.fields = new LinkedList<>();
        this.edges = new LinkedList<>();
    }

    public void addField(int[] field){
        fields.add(field);
        edges.add(new int[][]{field, {field[0] + 1, field[1]}});
        edges.add(new int[][]{field, {field[0] - 1, field[1]}});
        edges.add(new int[][]{field, {field[0], field[1] + 1}});
        edges.add(new int[][]{field, {field[0], field[1] - 1}});
    }

    public void print(){
        System.out.println(id + ": ");
        System.out.print("Fields: ");
        for(int[] f : fields){
            System.out.print("[" + f[0] + "|" + f[1] + "], ");
        }
        System.out.print("\nEdges: ");
        for(int[][] f : edges){
            System.out.print("[" + f[0][0] + "|" + f[0][1] + "||" + + f[1][0] + "|" + f[1][1] + "], ");
        }
        System.out.print("\n");
    }

    public int getArea(){
        return fields.size();
    }

    public int getCircumference(){
        return getDistinctOuterEdges().size();
    }

    public int getSidesCircumference(){
        int corners = 0;
        LinkedList<int[][]> outerEdges = getDistinctOuterEdges();
        LinkedList<int[]> padding = getPadding(outerEdges);
        for(int[] field : padding){
            
        }
    }

    private LinkedList<int[]> getPadding(LinkedList<int[][]> edges){
        LinkedList<int[]> res = new LinkedList<>();
        for(int[][] e : edges){
            if(this.fields.contains(e[0])){
                if(!res.contains(e[1])) res.add(e[1]);
            }
            else if(!res.contains(e[0])) res.add(e[0]);
        }
        return res;
    }

    /*private int countTriplets(LinkedList<int[][]> list){
        int res = 0;
        outer:
        for(int[][] e1 : list){
            for(int[][] e2 : list){
                if(!compareEdges(e1, e2)){
                    for(int[][] e3 : list){
                        if(!compareEdges(e1, e3) && !compareEdges(e2, e3)){
                            if(edgesOneApart(e1, e2)  && edgesOneApart(e2, e3)){
                                for(int[][] e4 : list){
                                    if(!compareEdges(e1, e4) && !compareEdges(e2, e4) && !compareEdges(e3, e4)){
                                        if(edgesOneApart(e1, e4) || edgesOneApart(e2, e4) || edgesOneApart(e3, e4)) break outer;
                                    }
                                }
                                res++;
                            }
                        }
                    }
                }
            }
        }
        return res;
    }

    private boolean hasEdgePartner(int[][] edge, LinkedList<int[][]> edgeList){
        for(int[][] e : edgeList){
            if(((e[0][0] == edge[0][0] && e[0][1] == edge[0][1]) || (e[1][0] == edge[1][0] && e[1][1] == edge[1][1])
                ) && !compareEdges(e, edge) && edgesOneApart(e, edge)) return true;
        }
        return false;
    }

    private boolean edgesOneApart(int[][] e1, int[][] e2){
        int[] temp;
        if(e1[0][0] == e2[1][0] && e1[0][1] == e2[1][1]){
            temp = e2[0];
            e2[0] = e2[1];
            e2[1] = temp;
        }
        return Math.abs(e1[1][0] - e2[1][0]) == 1 && Math.abs(e1[1][1] - e2[1][1]) == 1;
    }*/

    public LinkedList<int[][]> getDistinctOuterEdges(){
        LinkedList<int[][]> distinctEdges = new LinkedList<>();
        for(int[][] edge : edges){
            int contains = 0;
            for(int[][] e : edges){
                if(compareEdges(e, edge)){
                    contains++;
                    if(contains >= 2) break;
                }
            }
            if(contains < 2) distinctEdges.add(edge);
        }
        System.out.print("\nDistinct Edges: ");
        for(int[][] f : distinctEdges){
            System.out.print("[" + f[0][0] + "|" + f[0][1] + "||" + + f[1][0] + "|" + f[1][1] + "], ");
        }
        return distinctEdges;
    }

    private static boolean compareEdges(int[][] edge1, int[][] edge2){
        if(edge1[0][0] == edge2[0][0] && edge1[0][1] == edge2[0][1] && edge1[1][0] == edge2[1][0] && edge1[1][1] == edge2[1][1]) return true;
        if(edge1[0][0] == edge2[1][0] && edge1[0][1] == edge2[1][1] && edge1[1][0] == edge2[0][0] && edge1[1][1] == edge2[0][1]) return true;
        else return false;
    }

    public LinkedList<Garden> splitGarden(){
        LinkedList<Garden> res = new LinkedList<>();

        while(!this.fields.isEmpty()) {
            Garden newGarden = new Garden(this.id);
            newGarden.addField(this.fields.pop());
            boolean changed = true;
            while (changed) {
                changed = false;
                LinkedList<int[]> borderingFields = newGarden.getNeighbouringFields();
                for (int[] field : borderingFields) {
                    if (!listContains(newGarden.fields, field) && listContains(this.fields, field)) {
                        newGarden.addField(field);
                        this.removeField(field);
                        changed = true;
                    }
                }
            }
            res.add(newGarden);
        }
        return res;
    }

    public LinkedList<int[]> getNeighbouringFields(){
        LinkedList<int[]> res = new LinkedList<>();
        LinkedList<int[]> fieldsCopy = (LinkedList<int[]>) this.fields.clone();
        LinkedList<int[]> neighbours = new LinkedList<>();
        for(int[] field : fieldsCopy){
            neighbours.clear();
            if(field[0] != 0) neighbours.add(new int[]{field[0] - 1, field[1]});
            if(field[0] != day12.height - 1) neighbours.add(new int[]{field[0] + 1, field[1]});
            if(field[1] != 0) neighbours.add(new int[]{field[0], field[1] - 1});
            if(field[1] != day12.width - 1) neighbours.add(new int[]{field[0], field[1] + 1});
            for(int[] n : neighbours){
                if(!listContains(fieldsCopy, n) && !listContains(res, n)) res.add(n);
            }
        }
        return res;
    }

    private boolean listContains(LinkedList<int[]> list, int[] n){
        for(int[] l : list){
            if(l[0] == n[0] && l[1] == n [1]) return true;
        }
        return false;
    }

    public void removeField(int[] field){
        LinkedList<int[]> toRemove = new LinkedList<>();
        for(int[] f : fields){
            if(f[0] == field[0] && f[1] == field[1]) toRemove.add(f);
        }
        for(int[] f : toRemove){
            this.fields.remove(f);
        }
    }
}
