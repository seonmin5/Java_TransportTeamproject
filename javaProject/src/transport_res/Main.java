package transport_res;

import java.util.Scanner;

public class Main {
	
	private static int initialMoney;
	public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        while (true) {
        	System.out.print("현재 가지고 있는 돈을 입력해 주세요 (예: 4000) : ");
        	
        	if (sc.hasNextInt()) {
        		initialMoney = sc.nextInt();
        		sc.nextLine();
        		break;
        	} else {
        		System.out.println("잘못된 형식입니다. 숫자만 입력해 주세요.");
        		sc.nextLine();
        	}
        }
        
        
        while (true) {
        	Person person = new Person(0, 0, initialMoney);
        	
        	int startMinutes = insertTime(sc, "출발 시간을 입력해 주세요 (예: 1시 30분): ");
        	int endMinutes = insertTime(sc, "도착 시간을 입력해 주세요 (예: 2시 45분): ");

            System.out.print("목적지를 입력해 주세요 : ");
            String destination = sc.nextLine();

            person.setCurrentTime(startMinutes);
            person.setTargetTime(endMinutes);

            int timeDiff = person.diffTime();
            Transport vehicle = Transport.selectTransport(timeDiff, destination);

            if (vehicle != null) {
                boolean canRide = vehicle.ride(person);
                if (canRide) {
                    vehicle.getOff();
                } else {
                    person.incrementTardiness();
                }
            } else {
                person.incrementTardiness();
            }

            System.out.print("더 입력하시겠습니까? (y/n): ");
            String response = sc.next();     
            if (response.equalsIgnoreCase("n")) {
                break;
            }
        }

        sc.close();
    }
	
	// 출발, 도착 입력 메서드
	private static int insertTime(Scanner sc, String info) {
		while (true) {
    		try {
    			System.out.print(info);
    			 String time = sc.nextLine();
    			return convertToMinutes(time);        			
    		} catch(IllegalArgumentException e) {
    			System.out.println("다시 입력해 주세요.");
    		}
		}
	}
    
    // 출발, 도착 시간 예외처리
    private static int convertToMinutes(String time) {
        boolean isContainsHour = time.contains("시");
        boolean isContainsMinute = time.contains("분");
        
        // 0. 시, 분 입력 확인
        if (!isContainsHour || !isContainsMinute) {
        	throw new IllegalArgumentException("잘못된 시간 형식입니다: " + time);
        }
        
        int hourIndex = time.indexOf("시");
        int minuteIndex = time.indexOf("분");
        
        if (hourIndex == -1 || minuteIndex == -1 || hourIndex >= minuteIndex) {
            throw new IllegalArgumentException("잘못된 시간 형식입니다: " + time);
        }
        
        String hourPart = time.substring(0, hourIndex).trim();
        String minutePart = time.substring(hourIndex+1, minuteIndex).trim();
        
        // 1-1. 시간 형식 확인(길이)
        if (hourPart.length() > 2 || hourPart.length() < 1 || minutePart.length() > 2 || minutePart.length() < 1) {
        	throw new IllegalArgumentException("잘못된 시간 형식입니다: " + time);
        }
        
        // 1.2. 시간 형식 확인(숫자)
        for (int i = 0; i < hourPart.length(); i++) {
        	if (!(Character.isDigit(hourPart.charAt(i)))) {
            	throw new IllegalArgumentException("잘못된 시간 형식입니다: " + time);
        	}
        }
        
        for (int i = 0; i < minutePart.length(); i++) {
        	if (!(Character.isDigit(minutePart.charAt(i)))) {
            	throw new IllegalArgumentException("잘못된 시간 형식입니다: " + time);
        	}
        }
        
        int hours = Integer.parseInt(hourPart);
        int minutes = Integer.parseInt(minutePart);
        
        // 2. 시간 범위 확인
        if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
        	throw new IllegalArgumentException("잘못된 시간 형식입니다: " + time);
        }
        
        return hours * 60 + minutes;
    }

}
