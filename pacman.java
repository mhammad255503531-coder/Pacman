import java.util.Scanner;
//  ABSTRACT WALKER CLASS
abstract class Walker
{
    protected int x,y;
    protected int initialX,initialY;
    protected char symbol;
    public Walker(int x,int y,char symbol)
    {
        this.x=x;
        this.y=y;
        this.initialX=x;
        this.initialY=y;
        this.symbol=symbol;
    }
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public char getSymbol()
    {
        return symbol;
    }
    public void setX(int x)
    {
        this.x=x;
    }
    public void setY(int y)
    {
        this.y=y;
    }
    public void reset()
    {
        x=initialX;
        y=initialY;
    }
    public abstract void move();
}
// PACMAN CLASS
class DirectedWalker extends Walker
{
    private char dir='E';
    public DirectedWalker(int x,int y)
    {
        super(x,y,'P');
    }
    public void setDir(char d)
    {
        dir=d;
    }
    public char getDir()
    {
        return dir;
    }
    @Override
    public void move()
    {
        switch (dir)
        {
            case'N'->x--;
            case'S'->x++;
            case'E'->y++;
            case'W'->y--;
        }
    }
}
//GHOST CLASS
class RandomWalker extends Walker
{
    private final char[][] maze;
    public RandomWalker(int x,int y,char[][] maze)
    {
        super(x,y,'G');
        this.maze=maze;
    }
    @Override
    public void move()
    {
        int[] dx={-1, 1, 0, 0};   // N, S, W, E
        int[] dy={0, 0, -1, 1};
        for (int attempt=0;attempt<4;attempt++)
        {
            int d=(int)(Math.random()*4);
            int nx=x+dx[d];
            int ny=y+dy[d];
            if(nx>=0&&nx<20&&ny>=0&&ny<20&&maze[nx][ny]!='#')
            {
                x=nx;
                y=ny;
                return;
            }
        }

    }
}
// Driver class
class Driverclass
{
    private final int rows=20;
    private final int cols=20;
    private char[][] maze=new char[rows][cols];
    private DirectedWalker pacman;
    private RandomWalker[] ghosts;
    private int score=0;
    private int lives=3;
    private boolean gameOver=false;
    public  Driverclass()
    {
        setMaze();
        pacman=new DirectedWalker(1,1);
        ghosts=new RandomWalker[5];
        ghosts[0]=new RandomWalker(5,8,maze);
        ghosts[1]=new RandomWalker(11,10,maze);
        ghosts[2]=new RandomWalker(15,14,maze);
        ghosts[3]=new RandomWalker(3,15,maze);
        ghosts[4]=new RandomWalker(8,5,maze);
    }
    // MAZE SETUP
    private void setMaze()
    {
        // Fill maze with food dots
        for (int i=0;i<rows;i++)
        {
            for(int j=0;j<cols;j++)
            {
                maze[i][j]='.';
            }
        }
        // Create outer borders
        for(int i=0;i<rows;i++)
        {
            maze[i][0]='#';
            maze[i][cols-1]='#';
        }
        for (int j=0;j<cols;j++)
        {
            maze[0][j]='#';
            maze[rows-1][j]='#';
        }
        // Internal walls
        for(int i=1;i<rows-1;i++)
        {
            if(i!=1&&i!=18&&i!=19)
            {
                if(i!=7&&i!=12) maze[i][2]='#';
                if(i!=8&&i!=14) maze[i][cols-3]='#';
            }
        }
        // Specific wall placements
        maze[3][2]='#';maze[4][2]='#';maze[17][2]='#';
        maze[3][cols-3]='#';maze[4][cols-3]='#';maze[17][cols-3]='#';

        for(int j=4;j<cols-4;j++)
        {
            if(j!=8&&j!=14) maze[4][j]='#';
            if(j!=6&&j!=12) maze[16][j]='#';
        }
        // Box-like structures
        for(int j=4;j<=6;j++){maze[6][j]='#';}
        for(int i=6;i<=8;i++){maze[i][4]='#';}
        for(int j=13;j<=15;j++){maze[6][j]='#';}
        for(int i=6;i<=8;i++){ maze[i][15]='#';}
        for(int j=4;j<=6;j++){maze[12][j]='#';}
        for(int i=12;i<=14;i++){maze[i][4]='#';}
        for(int j=13;j<=15;j++){maze[12][j]='#';}
        for(int i=12;i<=14;i++){maze[i][13]='#';}
        maze[9][8]='#';maze[9][10]='#';maze[9][12]='#';
        maze[2][8]='#';maze[2][12]='#';
        maze[18][8]='#';maze[18][12]='#';
        placeBonuses();
    }
    //BONUS PLACEMENT
    private void placeBonuses()
    {
        int placed=0;
        int attempts=0;
        while(placed<4&&attempts<100)
        {
            int rx=1+(int)(Math.random()*(rows-2));
            int ry=1+(int)(Math.random()*(cols-2));
            if(maze[rx][ry]=='.')
            {
                maze[rx][ry]='★';
                placed++;
            }
            attempts++;
        }
    }
    //COUNT REMAINING FOOD
    private int countRemainingFood()
    {
        int count=0;
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<cols;j++)
            {
                if(maze[i][j]=='.'||maze[i][j]=='★')
                {
                    count++;
                }
            }
        }
        return count;
    }
    //DRAW METHOD
    public void draw()
    {
        // Draw the maze
        for(int i=0;i<rows;i++)
        {
            for(int j= 0;j<cols;j++)
            {
                if(pacman.getX()==i&&pacman.getY()==j)
                {
                    System.out.print("P ");
                }
                else
                {
                    boolean isGhost=false;
                    for(RandomWalker g:ghosts)
                    {
                        if(g.getX()==i&&g.getY()==j)
                        {
                            System.out.print("G ");
                            isGhost=true;
                            break;
                        }
                    }
                    if(!isGhost)
                    {
                        System.out.print(maze[i][j]+" ");
                    }
                }
            }
            System.out.println();
        }
        // Game status
        System.out.println("Score is="+score+"| Lives left="+lives +" | Food Left="+countRemainingFood());
        // Input prompt
        System.out.print("Move(w=up,a=left,s=down,d=right) or q=quit= ");
    }

    private boolean canMove(int x,int y)
    {
        if(x<0||x>=rows||y<0||y>=cols) return false;
        return maze[x][y]!='#';
    }

    private void checkCollision()
    {
        for(RandomWalker g:ghosts)
        {
            if(pacman.getX()==g.getX()&&pacman.getY()==g.getY())
            {
                System.out.println(" Collision! Pacman loses a life.");
                lives--;

                if(lives<=0)
                {
                    draw();
                    System.out.println("Game Over! Final Score ="+score);
                    gameOver=true;
                    return;
                }

                pacman.reset();
                for (RandomWalker gh:ghosts) gh.reset();
                System.out.println("Positions reset. Lives remaining="+lives);
                return;
            }
        }
    }

    public void run()
    {
        Scanner sc=new Scanner(System.in);
        while (!gameOver)
        {
            draw();
            String input=sc.nextLine().trim().toLowerCase();

            if(input.isEmpty()) continue;

            if(input.charAt(0)=='q')
            {
                System.out.println("Thanks for playing!");
                break;
            }

            char cmd=input.charAt(0);
            if(cmd!='w'&&cmd!='a'&&cmd!='s'&&cmd!='d')
            {
                System.out.println("Wrong key pressed! Please press w,a,s,d,q for pacman ");
                continue;
            }
            char newDir=switch (cmd)
            {
                case'w'->'N';
                case's'->'S';
                case'a'->'W';
                case'd'->'E';
                default->pacman.getDir();
            };
            pacman.setDir(newDir);
            int oldX=pacman.getX();
            int oldY=pacman.getY();
            pacman.move();
            checkCollision();
            if(gameOver) break;
            if (canMove(pacman.getX(),pacman.getY()))
            {
                int px=pacman.getX();
                int py=pacman.getY();
                if(maze[px][py]=='.')

                {
                    maze[px][py]=' ';
                    score+=1;
                }
                else if(maze[px][py]=='★')
                {
                    maze[px][py]=' ';
                    score+=3;
                    System.out.println("⭐ BONUS! Pacman Got +3 Points");
                }
            }
            else
            {
                pacman.setX(oldX);
                pacman.setY(oldY);
            }
            for(RandomWalker g:ghosts)
            {
                g.move();
            }
            checkCollision();
            // Win condition using dynamic count
            if(countRemainingFood()==0&&!gameOver)
            {
                draw();
                System.out.println("CONGRATULATIONS! YOU WON!");
                gameOver=true;
            }
        }
        sc.close();
    }
}
public class pacman
{
    public static void main(String[] args)
    {
        new Driverclass().run();
    }
}