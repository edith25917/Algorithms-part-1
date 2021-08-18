package com.myself;

import java.util.Arrays;
import java.util.Stack;

public class Board {

    private final int[][] boardTiles;
    private int blankI = 0;
    private int blankJ = 0;

    public Board(int[][] tiles){
        this.boardTiles = copy(tiles);
        this.findBlankPosition();
    }


    // string representation of this board
    public String toString(){
        String board = this.boardTiles.length+"\n";

        for(int i=0;i<boardTiles.length;i++){
            for(int j=0;j<boardTiles.length;j++){
                board += boardTiles[i][j]+"\t";
            }
            board += "\n";
        }

        return board;
    }

    // board dimension n
    public int dimension(){
        return this.boardTiles.length;
    }

    // number of tiles in the wrong position
    public int hamming(){
        int wrongPosition = 0;

        for(int i=0;i<boardTiles.length;i++){
            for(int j=0;j<boardTiles.length;j++){
                int correctNum = i*dimension()+j+1;
                boolean isLast = i == this.boardTiles.length-1 && j == this.boardTiles.length-1;

                if(!isLast && boardTiles[i][j] != correctNum){
                    wrongPosition++;
                }
            }
        }

        return wrongPosition;
    }

    // sum of Manhattan distances (vertical and horizontal distance)
    // from the tiles to their goal positions between tiles and goal
    public int manhattan(){

        int manhattanDistance = 0;

        for(int i=0;i<boardTiles.length;i++){
            for(int j=0;j<boardTiles.length;j++){
                boolean isLast = i == this.boardTiles.length-1 && j == this.boardTiles.length-1;

                int num = this.boardTiles[i][j];
                if(num==0){ //blank
                    continue;
                }
                int targetI = (num-1) / this.boardTiles.length;
                int targetJ = (num-1) % this.boardTiles.length;

                manhattanDistance += Math.abs(targetI-i)+Math.abs(targetJ-j);
            }
        }

        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal(){

        return hamming()==0;
    }

    // does this board equal y? (same size and their corresponding tiles are in the same positions.)
    public boolean equals(Object y){
        if(y == this){
            return true;
        }
        if(y == null) {
            return false;
        }
        if(y.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) y;

        return Arrays.deepEquals(this.boardTiles, that.boardTiles);
    }

    // all neighboring boards 2~4 neighbors, depending on the location of the blank square
    public Iterable<Board> neighbors(){
        Stack<Board> neighbors = new Stack<>();
        this.findBlankPosition();

        if(this.blankI>0){
            neighbors.push(createNeighbor(this.blankI-1, this.blankJ));
        }
        if(this.blankI<this.boardTiles.length-1){
            neighbors.push(createNeighbor(this.blankI+1, this.blankJ));
        }
        if(this.blankJ>0){
            neighbors.push(createNeighbor(this.blankI, this.blankJ-1));
        }
        if(this.blankJ<this.boardTiles.length-1){
            neighbors.push(createNeighbor(this.blankI, this.blankJ+1));
        }

        return neighbors;
    }

    private void findBlankPosition(){
        for(int i=0;i<this.boardTiles.length;i++){
            for(int j=0;j<this.boardTiles.length;j++){
                if(this.boardTiles[i][j]==0){
                    this.blankI = i;
                    this.blankJ = j;
                }
            }
        }
    }

    private Board createNeighbor(int i, int j){
        int[][] copy = copy(this.boardTiles);

        Board neighbor = new Board (copy);
        neighbor.swap(neighbor.boardTiles,this.blankI,this.blankJ,i,j);

        return neighbor;
    }

    private int[][] copy(int[][] tiles){
        int[][] copy = new int[tiles.length][];
        for(int k=0;k<tiles.length;k++){
            copy[k]=Arrays.copyOf(tiles[k], tiles[k].length);
        }

        return copy;
    }

    private void swap(int[][] tiles,int i1, int j1, int i2, int j2){
        int tmp = tiles[i1][j1];
        tiles[i1][j1] = tiles[i2][j2];
        tiles[i2][j2] = tmp;
    }


//    // a board that is obtained by exchanging any pair of tiles
    public Board twin(){
        int[][] tiles = copy(this.boardTiles);

        if(tiles[0][0]!=0&&tiles[0][1]!=0){
            swap(tiles,0,0,0,1);
        }else{
            swap(tiles,1,0,1,1);
        }

        return new Board(tiles);
    }


}
