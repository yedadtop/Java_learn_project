import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GameFrame extends JFrame implements KeyListener,MouseListener{
    private final int row=6;
    private final int col=6;
    private final int types=5;
    private final int width=400;
    private final int height=400;
    private final int [][]chessboard=new int[row][col];
    int pressi=0;
    int pressj=0;
    int releasei=0;
    int releasej=0;

    public GameFrame(){
        //初始化框架
        initJFrame();
        //初始化图片
        initimage();
        this.setVisible(true);
    }
    //下落图标
           private void drop() {
        Random r = new Random();
        // 下落逻辑
        for (int j = 0; j < col; j++) {
            boolean needGenerate = false;
            
            // 处理现有元素下落
            for (int i = row - 1; i >= 0; i--) {
                if (chessboard[i][j] == -1) {
                    // 向上查找可用元素
                    for (int k = i - 1; k >= 0; k--) {
                        if (chessboard[k][j] != -1) {
                            chessboard[i][j] = chessboard[k][j];
                            chessboard[k][j] = -1;
                            break;
                        }
                    }
                    // 标记需要生成新元素
                    if (chessboard[i][j] == -1) {
                        needGenerate = true;
                    }
                }
            }
            // 生成新元素
            if (needGenerate) {
                // 从顶部开始填充新元素
                int fillIndex = 0;
                for (int i = 0; i < row; i++) {
                    if (chessboard[i][j] == -1) {
                        chessboard[i][j] = r.nextInt(5) + 1;
                        // 新元素下落动画
                        for (int k = i; k > fillIndex; k--) {
                            chessboard[k][j] = chessboard[k-1][j];
                            chessboard[k-1][j] = -1;
                        }
                        fillIndex++;
                    }
                }
            }
        }
        // 刷新界面
        initimage();
    }

    //消除图标
    private void disappear() {
        //判断有无3个及以上连续
        int [][]dis=new int[row][col];
        for(int i=0;i<row;i++){
            for (int j = 0; j < col; j++) {
                //判断行
                if(j<row-2&&chessboard[i][j]==chessboard[i][j+1]&&chessboard[i][j]==chessboard[i][j+2]){
                        dis[i][j]=dis[i][j+1]=dis[i][j+2]=-1;
                }
                //判断列
                if(j<col-2&&chessboard[j][i]==chessboard[j+1][i]&&chessboard[j][i]==chessboard[j+2][i]){
                    dis[j][i]=dis[j+1][i]=dis[j+2][i]=-1;
                }
            }
        }
        //替换消除图片 - 使用空白图片实现移除效果
        for(int i=0;i<row;i++){
            for (int j = 0; j < col; j++) {
                if(dis[i][j]==-1){
                    // 通过组件名称查找对应标签
                    // 当dis[i][j]等于-1时，表示该位置的图片需要被消除
                    Component[] components = this.getContentPane().getComponents();
                    // 获取内容面板中的所有组件，返回一个Component数组
                    // 内容面板(Container)包含了所有的JLabel图片组件和背景
                    
                    for(Component comp : components) {
                        // 遍历所有组件，使用增强for循环语法
                        if(comp instanceof JLabel) {
                            // 判断当前组件是否是JLabel类型
                            // 因为我们只关心显示图片的JLabel，过滤掉其他类型的组件
                            JLabel label = (JLabel) comp;
                            // 将Component强制转换为JLabel类型，以便使用JLabel的特有方法
                            String name = label.getName();
                            // 获取该JLabel的名称，这个名称在initimage()方法中设置
                            if(name != null && name.equals(j + "." + i)) {
                                // 检查名称是否匹配当前要消除的位置
                                // 注意：这里应该是j+"."+i，因为设置名称时是i+"."+j
                                // 表示第j行第i列的图片
                                
                                label.setIcon(new ImageIcon("images/-1.png"));
                                // 将该JLabel的图标替换为空白图片
                                // 这样就实现了视觉上的"消除"效果

                                //原数组消除的图标赋值为0,方便下落
                                chessboard[i][j]=-1;
                                break;
                                // 找到并处理完成后跳出循环，提高效率
                            }
                        }
                    }
                }
            }
        }
        //查看消除元素是否重置为-1
        System.out.println("---------------------");
        for (int z = 0; z < 6; z++) {
            for (int x = 0; x < 6; x++) {
                System.out.print(chessboard[z][x]+" ");
            }
            System.out.println();
        }
        this.getContentPane().repaint();   // 立即重画
    }

    //加载图标
    private void initimage() {
        this.getContentPane().removeAll();
        //加载图标
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                ImageIcon icon=new ImageIcon("images/gifts/"+chessboard[j][i]+".png");
                JLabel jlimage=new JLabel(icon);
                jlimage.setBounds(48*i+50,48*j+50,48,48);
                jlimage.setBorder(new BevelBorder(BevelBorder.RAISED));
                jlimage.setName(i+"."+j);  // 添加这行设置名称
                jlimage.addMouseListener(this);  // 添加这行注册监听器
                this.getContentPane().add(jlimage);
            }
        }
        //背景
        JLabel Jlbg=new JLabel(new ImageIcon("images/game-bg.png"));
        Jlbg.setBounds(0,0,width,height);
        this.getContentPane().add(Jlbg);
        this.getContentPane().repaint();
    }

    private void initJFrame() {
        Random r=new Random();
        //填满二维数组
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                chessboard[i][j]=r.nextInt(1,types+1);
            }
        }
        this.setSize(width,height);
        this.setTitle("消消乐");
        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
        this.setLayout(null);
    }

    private void exchange(){
        int temp=chessboard[pressi][pressj];
        chessboard[pressi][pressj]=chessboard[releasei][releasej];
        chessboard[releasei][releasej]=temp;
        initimage();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode==87){
            disappear();
            drop();
            initimage();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    @Override
    public void mouseClicked(MouseEvent e) {
        disappear();
        drop();
        initimage();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        JComponent source = (JComponent) e.getSource();
        if(source.getName() != null) {
            pressj= Integer.parseInt(source.getName().split("\\.")[0]);
            pressi= Integer.parseInt(source.getName().split("\\.")[1]);

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("交换");
        exchange();
        disappear();
        drop();
        initimage();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        JComponent source = (JComponent) e.getSource();
        if(source.getName() != null) {
            releasej= Integer.parseInt(source.getName().split("\\.")[0]);
            releasei= Integer.parseInt(source.getName().split("\\.")[1]);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
        
