package com.myself;

import edu.princeton.cs.algs4.MinPQ;

import java.util.Stack;

public class Solver {

    private SearchNode current;
    private boolean isSolvable = false;
    private Stack<Board> solution;


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial){
        if(initial == null){
            throw new IllegalArgumentException();
        }
        this.solution = new Stack<>();
        MinPQ<SearchNode> gametree = new MinPQ<>();

        gametree.insert(new SearchNode(initial, 0,null));
        gametree.insert(new SearchNode(initial.twin(), 0, null));

        //確認是否為空
        while(true){
            //不為空 拿出現有 board,確認是否為goal board

            if(!gametree.min().board.isGoal()){
                //不是goal board, 插入neighbor
                this.current = gametree.delMin();

                this.current.board.neighbors().forEach(board ->{
                    //跟父節點相同就不加入
                    if(this.current.parent == null || this.current.parent != null && !board.equals(this.current.parent.board)){
                        //TODO 原本用++this.current.moves, 但moves答案有問題 待確認原因
                        gametree.insert(new SearchNode(board,this.current.moves+1,this.current));
                    }
                });
            }else{
                Stack<Board> tmp = new Stack<>();
                SearchNode cur = gametree.min();
                while (cur.parent!=null){
                    tmp.push(cur.board);
                    cur = cur.parent;
                }

                if(cur.board.equals(initial)){
                    this.isSolvable = true;
                    this.solution.push(initial);
                    while(!tmp.empty()){
                        this.solution.push(tmp.pop());
                    }
                }
                break;
            }
        }
    }


    // is the initial board solvable? (see below)
    public boolean isSolvable(){
        return this.isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves(){
        return this.isSolvable? this.solution.size()-1: -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution(){
        return this.isSolvable? this.solution:null;
    }

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;
        private SearchNode parent;
        private int priority = 0;
        private int manhattan = 0;

        public SearchNode(Board init, int moves,SearchNode prev) {
            this.board = init;
            this.parent = prev;
            this.moves = moves;
            this.priority = (this.board.manhattan()+this.moves);
            this.manhattan = this.board.manhattan();
        }

        @Override
        public int compareTo(SearchNode that) {
            //manhattan priority function = manhattan + moves made so far
            int priority = this.priority - (that.manhattan+that.moves);
            if(priority == 0){
                priority = this.manhattan-that.manhattan;
            }
            return priority;
        }
    }


}