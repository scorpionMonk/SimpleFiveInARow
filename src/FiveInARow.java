package com.scorpionhermit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FiveInARow extends JFrame implements MouseListener {
	Vector v = new Vector();	//所有的每步走棋信息
	Vector white = new Vector(); //白方走棋信息
	Vector black = new Vector(); //黑方走棋信息
	boolean b; //用来判断白旗还是黑棋
	int whiteCount, blackCount; //计算悔棋步数
	int w = 25;	//间距大小
	int px = 100, py = 100;	//棋盘的大小
	int pxw = px + w, pyw = py + w;
	int width = w * 16, height = w * 16;
	int vline = width + px;	//垂直线的长度
	int hline = height + py; //水平线的长度
	
	/**
	 * 构造方法
	 */
	public FiveInARow() {
		super("单机版五子棋");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭按钮
		Container con = this.getContentPane();
		con.setLayout(new BorderLayout());
		this.addMouseListener(this);//添加监听
		this.setSize(600, 600);//设置窗体大小
		this.setBackground(Color.orange);
		this.setVisible(true);
	}
	
	/**
	 * 画棋盘和棋子
	 * @param e
	 */
	public void paint(Graphics g) {
		g.clearRect(0, 0, this.getWidth(), this.getHeight());//清除画板
		g.setColor(Color.BLACK);//绘制网格颜色
		g.drawRect(px, py, width, height);//网格大小
		g.drawString("单机版五子棋小游戏，右击可以悔棋，欢迎使用", 180, 70);
		
		for (int i=0; i<15; i++) {
			g.drawLine(pxw+i*w, py, pxw+i*w, hline);//每条横线和竖线
			g.drawLine(px, pyw+i*w, vline, pyw+i*w);
		}
		
		for (int x=0; x<v.size(); x++) {
			String str = (String)v.get(x);
			String tmp[] = str.split("-");
			int a = Integer.parseInt(tmp[0]);
			int b = Integer.parseInt(tmp[1]);
			a = a * w + px;
			b = b * w + py;
			if (x%2 == 0) {
				g.setColor(Color.WHITE);
			} else {
				g.setColor(Color.BLACK);
			}
			g.fillArc(a-w/2, b-w/2, w, w, 0, 360);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == e.BUTTON1) {
			int x = e.getX();
			int y = e.getY();
			x = (x - x % w) + (x % w > w / 2 ? w : 0);
			y = (y - y % w) + (y % w > w / 2 ? w : 0);
			x = (x - px) / w;
			y = (y - py) / w;
			
			if (x >= 0 && y >= 0 && x <= 16 && y <= 16) {
				if (v.contains(x+"-"+y)) {
					System.out.println("已经有棋了！");
				} else {
					v.add(x+"-"+y);
					this.repaint();
					if (v.size() % 2 == 0) {
						black.add(x+"-"+y);
						this.victory(x, y, black);
//						System.out.println("黑棋");
					} else {
						white.add(x+"-"+y);
						this.victory(x, y, white);
//						System.out.println("白棋");
					}
//					System.out.println(e.getX()+"-"+e.getY());
				}
			} else {
//				System.out.println(e.getX()+"-"+e.getY()+"|"+x+"-"+y+"\t超出边界了");
			}
		}
		
		if (e.getButton() == e.BUTTON3) {	//右击悔棋的方法
//			System.out.println("鼠标右击--悔棋");
			if (v.isEmpty()) {
				JOptionPane.showMessageDialog(this, "没有棋可悔");
			} else {
				if (v.size() % 2 == 0) {	//判断是白棋悔棋，还是黑棋悔棋
					blackCount++;
					if (blackCount > 3) {
						JOptionPane.showMessageDialog(this, "黑棋已经悔了3步");
					} else {
						v.remove(v.lastElement());
						this.repaint();
					}
				} else {
					whiteCount++;
					if (whiteCount > 3) {
						JOptionPane.showMessageDialog(this, "白棋已经悔了3步");
					} else {
						v.remove(v.lastElement());
						this.repaint();
					}
				}
			}
		}
	}

	/**
	 * 判断胜利的方法
	 * @param x
	 * @param y
	 * @param contain
	 */
	private void victory(int x, int y, Vector contain) {
		int cv = 0;	//垂直方向棋子数量
		int ch = 0;	//水平方向棋子数量
		int ci1 = 0; //斜面方向棋子数量1
		int ci2 = 0; //斜面方向棋子数量2
		
		//计算水平方向棋子数量
		for (int i=1; i<5; i++) {
			if (contain.contains((x+i)+"-"+y)) {
				ch++;
			} else {
				break;
			}
		}
		for (int i=1; i<5; i++) {
			if (contain.contains((x-i)+"-"+y)) {
				ch++;
			} else {
				break;
			}
		}
		
		//计算垂直方向棋子数量
		for (int i=1; i<5; i++) {
			if (contain.contains(x+"-"+(y+i))) {
				cv++;
			} else {
				break;
			}
		}
		for (int i=1; i<5; i++) {
			if (contain.contains(x+"-"+(y-i))) {
				cv++;
			} else {
				break;
			}
		}
		
		//计算45°斜面方向棋子数量
		for (int i=1; i<5; i++) {
			if (contain.contains((x+i)+"-"+(y+i))) {
				ci1++;
			} else {
				break;
			}
		}
		for (int i=1; i<5; i++) {
			if (contain.contains((x-i)+"-"+(y-i))) {
				ci1++;
			} else {
				break;
			}
		}
		
		//计算135°斜面方向棋子数量
		for (int i=1; i<5; i++) {
			if (contain.contains((x+i)+"-"+(y-i))) {
				ci2++;
			} else {
				break;
			}
		}
		for (int i=1; i<5; i++) {
			if (contain.contains((x-i)+"-"+(y+i))) {
				ci2++;
			} else {
				break;
			}
		}
		
		if (ch>=4 || cv>=4 ||ci1>=4 ||ci2>=4) {
			System.out.println(v.size()+"步棋");
			if (v.size() % 2 == 0) {
				//判断是黑棋赢，还是白棋赢
				JOptionPane.showMessageDialog(null, "黑棋赢了");
			} else {
				JOptionPane.showMessageDialog(null, "白棋赢了");
			}
			this.v.clear();
			this.black.clear();
			this.white.clear();
			this.repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
