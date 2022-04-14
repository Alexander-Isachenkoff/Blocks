package project.generation;

public class BoolMap {
    
    private final boolean[][] struct;
    
    public BoolMap(int height, int width) {
        struct = new boolean[height][width];
    }
    
    public BoolMap(int height, int width, int quantity) {
        struct = new boolean[height][width];
        int elements = 0;
        int x = Generator.getInt(0, width - 1);
        int y = Generator.getInt(0, height - 1);
        while (elements < quantity) {
            if (!struct[y][x]) {
                struct[y][x] = true;
                elements++;
            }
            switch (Generator.getInt(0, 3)) {
                case 0:
                    if (y > 0)
                        y--;
                    break;
                case 1:
                    if (y < struct.length - 1)
                        y++;
                    break;
                case 2:
                    if (x > 0)
                        x--;
                    break;
                case 3:
                    if (x < struct[0].length - 1)
                        x++;
                    break;
            }
        }
    }
    
    public int getWidth() {
        return struct[0].length;
    }
    
    public int getHeight() {
        return struct.length;
    }
    
    public boolean get(int i, int j) {
        return struct[i][j];
    }
    
    public void set(int i, int j, boolean value) {
        struct[i][j] = value;
    }
    
    public BoolMap shiftUp() {
        BoolMap newStruct = new BoolMap(struct.length, struct[0].length);
        for (int j = 0; j < struct[0].length; j++)
            if (struct[0][j])
                return this;
        for (int i = 1; i < struct.length; i++) {
            for (int j = 0; j < struct.length; j++) {
                newStruct.set(i - 1, j, struct[i][j]);
                newStruct.set(i, j, false);
            }
        }
        return newStruct.shiftUp();
    }
    
    public BoolMap shiftLeft() {
        BoolMap newStruct = new BoolMap(struct.length, struct[0].length);
        for (boolean[] elements : struct)
            if (elements[0]) {
                return this;
            }
        for (int j = 1; j < struct[0].length; j++) {
            for (int i = 0; i < struct.length; i++) {
                newStruct.set(i, j - 1, struct[i][j]);
                newStruct.set(i, j, false);
            }
        }
        return newStruct.shiftLeft();
    }
    
    public BoolMap rotate() {
        BoolMap newStruct = new BoolMap(struct[0].length, struct.length);
        for (int i = 0; i < struct.length; i++) {
            for (int j = 0; j < struct[i].length; j++) {
                newStruct.set(j, struct[i].length - 1 - i, struct[i][j]);
            }
        }
        return newStruct;
    }
}
