package soccer;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

	public static Scanner sc = new Scanner(System.in);
	public static final int INPUT = 1, UPDATE = 2, DELETE = 3, SEARCH = 4, OUTPUT = 5, SORT = 6, STATS = 7, EXIT = 8;

	public static void main(String[] args) {

		DBConnection dbc = new DBConnection();

		// Database connection
		dbc.connect();

		// �޴�����
		boolean flag = false;

		while (!flag) {

			int num = displayMenu();

			switch (num) {
			case INPUT:
				playerInputData();
				break;
			case UPDATE:
				UpdatePlayerData();
				break;
			case DELETE:
				deletePlayerData();
				break;
			case SEARCH:
				searchPlayerData();
				break;
			case OUTPUT:
				playerOutput();
				break;
			case SORT:
				sortPlayerData();
				break;
			case STATS:
				statictPlayerData();
				break;
			case EXIT:
				flag = true;
				break;
			default:
				System.out.println("1~7���߿� �������ּ���.");
				break;
			}

		} // end of while

		System.out.println("�ý��� ����");

	}

	private static void statictPlayerData() {

		List<SoccerPlayer> list = new ArrayList<SoccerPlayer>();

		try {
			System.out.print("������ : 1 , ��ÿ� : 2 , ����÷��̾� : 3 >> ");
			int type = sc.nextInt();

			boolean value = checkInputPattern(String.valueOf(type), 5);
			if (!value)
				return;

			DBConnection dbc = new DBConnection();
			dbc.connect();

			list = dbc.selectMaxMin(type);

			if (list.size() <= 0) {
				System.out.println("�˻��� ���������� �����ϴ�." + list.size());
				return;
			}
			
			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("Ÿ���� ���� �ʽ��ϴ�. ���Է¿�û" + e.getMessage());
			return;
		} catch (Exception e) {
			System.out.println("�����ͺ��̽� �л���� ����" + e.getMessage());
		}

	}

	// ����
	private static void sortPlayerData() {
		List<SoccerPlayer> list = new ArrayList<SoccerPlayer>();

		try {
			DBConnection dbc = new DBConnection();
			dbc.connect();

			// ������ �л� ��ȣ �Է�
			System.out.print("���Ĺ�ļ���(1.goal 2.assist 3.point) >> ");
			int type = sc.nextInt();

			// ��ȣ ���ϰ˻�
			boolean value = checkInputPattern(String.valueOf(type), 4);
			if (!value)
				return;

			list = dbc.selectOrderBy(type);

			if (list.size() <= 0) {
				System.out.println("������ list�� �����ϴ�." + list.size());
				return;
			}
			System.out.println("�̸�\t��\t��\t��ý�Ʈ\t�Ŀ�\t��������Ʈ\t���");
			for (SoccerPlayer soccerPlayer : list) {
				System.out.println(soccerPlayer);
			}
			dbc.close();

		} catch (Exception e) {
			System.out.println("�����ͺ��̽� ���� ����" + e.getMessage());
		}
		return;

	}

	// ����
	public static void UpdatePlayerData() {
		List<SoccerPlayer> list = new ArrayList<SoccerPlayer>();
		try {
			// ������ �л� ��ȣ �Ԥ���
			System.out.print("���� �̸� �Է� >> ");
			String name = sc.nextLine();
			// ��ȣ ���ϰ˻�
			boolean value = checkInputPattern(name, 2);
			if (!value)
				return;

			
			DBConnection dbc = new DBConnection();
			// Database connection
			dbc.connect();
			// Entering article table data
			list = dbc.selectSearch(name);

			if (list.size() <= 0) {
				System.out.println("�Էµ� ������ �����ϴ�.");
			}

			// ����Ʈ ������ �����ش�.
			System.out.println("�̸�\t��\t��\t��ý�Ʈ\t�Ŀ�\t��������Ʈ\t���");
			for (SoccerPlayer soccerPlayer : list) {
				System.out.println(soccerPlayer);
			}

			// ������ ����Ʈ�� ������� �ȴ�.
			SoccerPlayer imsiSoccerPlayer = list.get(0);
			System.out.print("�� �� �Է� >>");
			int goal = sc.nextInt();
			value = checkInputPattern(String.valueOf(goal), 3);
			if (!value)
				return;
			imsiSoccerPlayer.setGoal(goal);

			System.out.print("��ý�Ʈ �� �Է� >>");
			int assist = sc.nextInt();
			value = checkInputPattern(String.valueOf(assist), 3);
			if (!value)
				return;
			imsiSoccerPlayer.setAssist(assist);

			System.out.print("�Ŀ� �� �Է� >>");
			int foul = sc.nextInt();
			value = checkInputPattern(String.valueOf(foul), 3);
			if (!value)
				return;
			imsiSoccerPlayer.setFoul(foul);
			
			
			imsiSoccerPlayer.calPoint();

			// �����ͺ��̽� ������ �κ��� update ����
			int returnUpdateValue = dbc.update(imsiSoccerPlayer);
			if (returnUpdateValue == -1) {
				System.out.println("���� ���� ���� ����");
				return;
			}
			System.out.println("���� ���� ���� �Ϸ��Ͽ����ϴ�.");

			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("�Է� Ÿ�� ���� ����. �ٽ� �Է��ϼ���");
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("�����ͺ��̽� ���� ���� . �ٽ� �Է��ϼ���");
			return;
		}
	}

	// �˻�
	private static void searchPlayerData() {
		List<SoccerPlayer> list = new ArrayList<SoccerPlayer>();

		try {
			System.out.print("�˻��� ���� �̸��� �Է��ϼ��� : ");
			String name = sc.nextLine();

			boolean value = checkInputPattern(name, 2);
			if (!value) {
				return;
			}

			DBConnection dbc = new DBConnection();
			dbc.connect();
			list = dbc.selectSearch(name);
			if (list.size() <= 0) {
				System.out.println("������ list�� �����ϴ�." + list.size());
				return;
			}
			System.out.println("�̸�\t��\t��\t��ý�Ʈ\t�Ŀ�\t��������Ʈ\t���");
			for (SoccerPlayer soccerPlayer : list) {
				System.out.println(soccerPlayer);
			}
			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("Ÿ���� ���� �ʽ��ϴ�. ���Է¿�û" + e.getStackTrace());
			return;
		} catch (Exception e) {
			System.out.println("�����ͺ��̽� �˻� ����" + e.getStackTrace());
		}
	}

	// ���
	private static void playerOutput() {
		List<SoccerPlayer> list = new ArrayList<SoccerPlayer>();
		try {

			DBConnection dbc = new DBConnection();
			dbc.connect();
			list = dbc.select();
			if (list.size() <= 0) {
				System.out.println("������ list�� �����ϴ�." + list.size());
				return;
			}
			System.out.println("�̸�\t��\t��\t��ý�Ʈ\t�Ŀ�\t��������Ʈ\t���");
			for (SoccerPlayer soccerPlayer : list) {
				System.out.println(soccerPlayer);
			}
			dbc.close();

		} catch (Exception e) {
			System.out.println("�����ͺ��̽� ��� ����" + e.getMessage());
		}
		return;
	}

	// ����
	private static void deletePlayerData() {
		try {
			System.out.print("������ ������ �Է����ּ��� : ");
			String name = sc.nextLine();

			boolean value = checkInputPattern(name, 2);

			if (!value)
				return;

			DBConnection dbc = new DBConnection();
			dbc.connect();
			int insertReturnValue = dbc.delete(name);
			if (insertReturnValue == -1) {
				System.out.println("���������Դϴ�." + insertReturnValue);
			}
			if (insertReturnValue == 0) {
				System.out.println("������ ������ �������� �ʽ��ϴ�." + insertReturnValue);
			} else {
				System.out.println("���������Դϴ�. ���ϰ� = " + insertReturnValue);
			}

			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("Ÿ���� ���� �ʽ��ϴ�. ���Է¿�û" + e.getStackTrace());
			return;
		} catch (Exception e) {
			System.out.println("�����ͺ��̽� ���� ����" + e.getStackTrace());
		}
	}

	// �Է�
	private static void playerInputData() {

		String pattern = null;
		boolean regex = false;

		try {

			System.out.print("�̸��Է� : ");
			String name = sc.nextLine();
			boolean value = checkInputPattern(name, 2);
			if (!value)
				return;

			System.out.print("���Է� : ");
			String team = sc.nextLine();
			value = checkInputPattern(team, 2);
			if (!value)
				return;

			System.out.print("goal�Է� : ");
			int goal = sc.nextInt();
			value = checkInputPattern(String.valueOf(goal), 3);
			if (!value)
				return;

			System.out.print("assist�Է� : ");
			int assist = sc.nextInt();
			value = checkInputPattern(String.valueOf(assist), 3);
			if (!value)
				return;

			System.out.print("foul�Է� : ");
			int foul = sc.nextInt();
			value = checkInputPattern(String.valueOf(foul), 3);
			if (!value)
				return;

			// �����ͺ��̽� �Է�
			SoccerPlayer soccerPlayer = new SoccerPlayer(name, team, goal, assist, foul);
			soccerPlayer.calPoint();

			DBConnection dbc = new DBConnection();

			dbc.connect();

			int insertReturnValue = dbc.insert(soccerPlayer);

			if (insertReturnValue == -1) {
				System.out.println("���Խ����Դϴ�.");
			} else {
				System.out.println("���Լ����Դϴ�. ���ϰ�=" + insertReturnValue);
			}
			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("�Է�Ÿ���� ���� �ʽ��ϴ�. ���Է¿�û" + e.getStackTrace());
			return;
		} catch (Exception e) {
			System.out.println("�����ͺ��̽� �Է� ����" + e.getStackTrace());
		} finally {
			sc.nextLine();
		}

	}

	// �޴�����
	public static int displayMenu() {
		int num = -1;

		try {
			System.out.print("1.�Է� 2.���� 3.���� 4.�˻� 5.��� 6.���� 7.��� 8.���� : ");
			num = sc.nextInt();

			String pattern = "^[1-8]$";
			boolean regex = Pattern.matches(pattern, String.valueOf(num));
		} catch (InputMismatchException e) {
			System.out.println();
			System.out.println("���ڷ� �Է����ּ���");
			num = -1;
		} finally {
			sc.nextLine();
		}
		return num;
	}

	private static boolean checkInputPattern(String data, int patternType) {
		String pattern = null;
		boolean regex = false;
		String message = null;
		switch (patternType) {
		case 2:
			pattern = "^[��-�R]{2,10}$";
			message = "name ���Է¿��";
			break;
		case 3:
			pattern = "^[0-9]{1,3}$";
			message = "score ���Է¿��";

			break;
		case 4:
			pattern = "^[1-3]$";
			message = "����Ÿ�� ���Է¿��";
			break;
		case 5:
			pattern = "^[1-3]$";
			message = "���Ÿ�� ���Է¿��";
		
		}

		regex = Pattern.matches(pattern, data);
		if (patternType == 3) {
			if (Integer.parseInt(data) < 0 || Integer.parseInt(data) > 100) {
				System.out.println(message);
				return false;
			}
		} else {
			if (!regex) {
				System.out.println(message);
				return false;
			}
		}
		return regex;
	}
}
