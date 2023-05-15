SOLID 원칙
===
SOLID 원칙은 로버트 마틴이라는 사람이 명명한 객체 지향 프로그래밍 및 설계의 5가지 기본 원칙
시간이 지나도 유지 보수와 확장이 쉬운 시스템을 만들고자 할 때 이 원칙들을 함께 적용하여 프로그래머가 소스 코드가 읽기 쉽고 확장하기 쉽도록 설계하기 위해 원칙을 적용하여 개발한다.

<br>

>S : 단일 책임 원칙(Single responsibility principle, SRP)   
O : 개방-폐쇄 원칙(open-closed princicle, OCP)  
L : 리스코프 치환 원칙(Liskov subsitution principle, LSP)  
I : 인터페이스 분리 원칙(Interface segregation principle, ISP)  
D : 의존관계 역전 원칙 (Dependency inversion principle, DIP)


SRP - 단일 책임 원칙
---
> 어떤 클래스를 변경해야 하는 이유는 오직 하나뿐이어야 한다.

- 하나의 클래스는 하나의 책임만 가져야 한다.
- 하나의 클래스가 모든 책임을 갖고 있는 것이 아니라, 하위 클래스들에게 책임을 넘겨주면 테스트가 용이해지고 기능도 분리가 가능하다
- 애플리케이션 모듈 전반에서 높은 유지보수성과 가시성 제어 기능을 유지하는 원칙이다.

<br>

SRP 예제
-

```java
public class Production {

    private String name;
    private int price;

    public Production(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public void updatePrice(int price) {
        this.price = price;
    }
}


public class ProductionUpdateService {

    public void update(Production production, int price) {
        //validate price
        validatePrice(price);

        //update price
        production.updatePrice(price);
    }

    private void validatePrice(int price) {
        if (price < 1000) {
            throw new IllegalArgumentException("최소가격은 1000원 이상입니다.");
        }
    }

}
```
상품의 정보 변경을 책임지고 있는 ProductionUpdateService 에서 상품의 유효성까지 확인하는 책임이 있을 이유가 없으니 SRP를 적용하여 수정하면

```java
//SPR 적용 책임 분리
public class Production {

    private static final int MINIMUM_PRICE = 1000;

    private String name;
    private int price;

    public Production(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public void updatePrice(int price) {
        validatePrice(price);
        this.price = price;
    }

    private void validatePrice(int price) {
        if (price < MINIMUM_PRICE) {
            throw new IllegalArgumentException(String.format("최소가격은 %d원 이상입니다.", MINIMUM_PRICE));
        }
    }
}

public class ProductionUpdateService {

    public void update(Production production, int price) {
        //update price
        production.updatePrice(price);
    }

}
```
유효성 검증의 책임을 Production으로 옮김으로써 ProductionUpdateService는 온전히 상품의 정보를 변경하기 위한 코드만 존재하게 되었습니다.

<br>

OCP - 개방 폐쇄 원칙
----
> 소프트웨어 요소는 확장에는 열려 있으나 변경에는 닫혀 있어야 한다.

- 소프트웨어 개체(클래스, 모듈, 함수 등)는 확장에 대해서는 열려 있어야 하지만 수정에 대해서는 닫혀 있어야 한다.
- 다른 개발자가 작업을 수행하기 위해 반드시 수정해야 하는 제약 사항을 클래스에 포함해서는 안되고, 다른 개발자가 클래스를 확장하기만 하면 원하는 작업을 할 수 있도록 해야한다.
- 다양하고 직관적이며 유해하지 않은 방식으로 소프트웨어 확장성을 유지한다.

<br>

OCP 예제
---
```java
public class Production {
    private String name;
    private int price;
    // N(일반) ,E(전자티켓) ,L(지역상품)...
    private String option;

    public Production(String name, int price, String option) {
        this.name = name;
        this.price = price;
        this.option = option;
    }

    public int getNameLength() {
        return name.length();
    }

    public String getOption() {
        return option;
    }
}

///추가 요구사항(option)
public class ProductionValidator {
    public void validateProduction(Production production) throws IllegalAccessException {
        if (production.getNameLength() < 3) {
            throw new IllegalAccessException("일반 상품의 이름은 3글자보다 길어야 합니다.");
        }
    }
}
```
여기서 요구사항이 더 늘어나면 ProductionValidator에 추가, 변경, 삭제 통합 등 코드 수정이 번번하게 일어나고

```java
public class ProductionValidator {
    public void validateProduction(Production production) throws IllegalArgumentException {

        if (production.getOption().equals("N")) {
            if (production.getNameLength() < 3) {
                throw new IllegalArgumentException("일반 상품의 이름은 3글자보다 길어야 합니다.");
            }
        } else if (production.getOption().equals("E")) {
            if (production.getNameLength() < 10) {
                throw new IllegalArgumentException("전자티켓 상품의 이름은 10글자보다 길어야 합니다.");
            }
        } else if (production.getOption().equals("L")) {
            if (production.getNameLength() < 20) {
                throw new IllegalArgumentException("지역 상품의 이름은 20글자보다 길어야 합니다.");
            }
        }
        
    }
}
```
위와 같이 if-else문이 계속 추가되는 상황이 나올 수 있습니다. 그러므로 OCP를 지키는 구조를 취하면
```java
public interface Validator {

    boolean support(Production production);

    void validate(Production production) throws IllegalArgumentException;

}
```
검증 작업 용 인터페이스를 작성하고
```java
public class DefaultValidator implements Validator {
    @Override
    public boolean support(Production production) {
        return production.getOption().equals("N");
    }

    @Override
    public void validate(Production production) throws IllegalArgumentException {
        if (production.getNameLength() < 3) {
            throw new IllegalArgumentException("일반 상품의 이름은 3글자보다 길어야 합니다.");
        }
    }
}


public class ETicketValidator implements Validator {
    @Override
    public boolean support(Production production) {
        return production.getOption().equals("E");
    }

    @Override
    public void validate(Production production) throws IllegalArgumentException {
        if (production.getNameLength() < 10) {
            throw new IllegalArgumentException("전자티켓 상품의 이름은 10글자보다 길어야 합니다.");
        }
    }
}

public class LocalValidator implements Validator {
    @Override
    public boolean support(Production production) {
        return production.getOption().equals("L");
    }

    @Override
    public void validate(Production production) throws IllegalArgumentException {
        if (production.getNameLength() < 20) {
            throw new IllegalArgumentException("지역 상품의 이름은 20글자보다 길어야 합니다.");
        }
    }
}
```
옵션에 대한 검증 작업을 담당하게 할 Validator을 구현하면
```java
public class ProductValidator {

    private final List<Validator> validators = Arrays.asList(new DefaultValidator(), new ETicketValidator(), new LocalValidator());

    public void validate(Production production) {
        Validator productionValidator = new DefaultValidator();

        for (Validator localValidator : validators) {
            if (localValidator.support(production)) {
                productionValidator = localValidator;
                break;
            }
        }

        productionValidator.validate(production);
    }
}
```

새로운 옵션이 생성되어 검증 로직이 추가되야 할 때 OCP를 지키지 않은 구조와 달리 ProductValidator 의 validate() 의 수정 없이 해당 검증을 담당할 객체를 추가하여 요구사항을 충족시킬 수 있습니다.

<br>

LSP - 리스코프 치환 원칙
---

> 서브 타입은 언제나 자신의 기반 타입(base type)으로 교체할 수 있어야 한다.
- 리스코프 치환 원칙으로 파생 타입은 반드시 기본 타입을 완벽하게 대체할 수 있어야 한다.
- 서브클래스의 객체는 슈퍼클래스의 객체와 반드시 같은 방식으로 동작해야한다.
- 타입 변환 후에 뒤따라오는 런타임 식별에 유용한 원칙이다.


> 하위 클래스 is a kind of 상위 클래스 - 하위 분류는 상위 분류의 한 종류다.  
구현 글래스 is able to "인터페이스" - 구현 분류는 "인터페이스"할 수 있어야 한다.

***인터페이스 할 수 있어야한다는 인터페이스 이름을 명명한 것에 따라 부르면 자연스럽다.***
위 두개의 문장대로 구현된 프로그램 이라면 리스코프 원칙을 잘 지키고 있다는 것이다.


<br>

리스코프 치환 원칙 사례
---

사각형의 넓이를 구하는 공식은 높이 * 폭 입니다. 그리고 이는 일반적인 책임이다. <br>
하지만 정사각형의 넓이를 구하는 공식은 높이 * 높이도 되고 폭 * 폭도 되듯 높이와 폭의 차이가 없다는것이다. <br>
정사각형의 넓이를 구하는 공식은 사각형의 넓이를 구하는 공식은 본질적으로는 같으나 비본질적으론 다른것이다. <br>


```java
public class Rectangle {

	private double width;
	private double height;
	
	public void setWidth(double width) {
		this.width = width;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	
	public double getArea() {
		return this.width * this.height;
	}
}

public class Square extends Rectangle{

	@Override
	public void setWidth(double width) {
		super.setWidth(width);
		super.setHeight(width);
	}

	@Override
	public void setHeight(double height) {
		super.setHeight(height);
		super.setWidth(height);
	}
}

public static void main(String[] args){

	Rectangle r = new Square();
	r.setHeight(3);
	r.setWidth(2);
	System.out.println(r.getArea());
	
}
```
>위의 코드로 넓이를 구하면 6을 기대하지만 결과는 4로 나온다.

하위 타입에 의존하여(정사각형이냐 직사각형이냐에 따라) 행위(폭과 높이의 길이를 각각 설정할 수 있는지의 행위)가 변경되기 때문이다. 즉 하위 타입으로 치환이 되질 않는 것이다.

그러므로 
> 하위 클래스의 인스턴스는 상위형 객체 참조 변수에 대입해 상위 클래스의 역할을 하는 데 문제가 없어야 한다.
>> 하위형에서 선행 조건은 강화될 수 없다.  
하위형에서 후행 조건은 약화될 수 없다.  
하위형에서 상위형의 불변 조건은 반드시 유지돼야 한다.


<br>

ISP - 인터페이스 분리 원칙
---

> 특정 클라이언트를 위한 인터페이스 여러 개가 범용 인터페이스 하나보다 낫다

- 인터페이스 분리 원칙으로 클라이언트가 사용하지 않을 불필요한 메소드를 강제로 구현하게 해서는 안된다.
- 클라이언트가 사용하지 않을 메소드를 강제로 구현하는 일이 없을 때까지 하나의 인터페이스를 2개 이상의 인터페이스로 분할하는 원칙

<br>

인터페이스 분리 원칙 위반 예시
---
```java 
//복합기의 기능을 가지고 있는 인터페이스
public interface AllInOneDevice {
    void print();

    void copy();

    void fax();
}

//위의 인터페이스를 사용하여 구현
public class SmartMachine implements AllInOneDevice {
    @Override
    public void print() {
        System.out.println("print");
    }

    @Override
    public void copy() {
        System.out.println("copy");
    }

    @Override
    public void fax() {
        System.out.println("fax");
    }
}
```
여기서 인쇄기능만 필요한 인쇄기 구현해보면
```java
//인쇄 기능만 필요한 인쇄기 구현
public class PrinterMachine implements AllInOneDevice {
    @Override
    public void print() {
        System.out.println("print");
    }

    @Override
    public void copy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void fax() {
        throw new UnsupportedOperationException();
    }
}
```
필요한 Print는 override되었지만 나머지 기능은 구현할 필요가 없기 때문에 UnsupportedOperationException를 발생 시키고 있습니다. <br>

이렇게 구현된 객체는 자신에게 필요없는 책임(copy, fax)를 가지고 있어 SOLID의 첫번째 원칙인 SRP도 어기고 있는것을 확인할 수 있습니다. <br>
해결책은 ISP의 이름에도 나와 있듯 하나의 인터페이스를 분리하여 여러개의 인터페이스로 나누는 것입니다.
인터페이스 분리 원칙 올바른 예시
---

```java
//기능 interface 구현
public interface PrinterDevice {
    void print();
}

public interface CopyDevice {
    void copy();
}

public interface FaxDevice {
    void fax();
}

//복합기 구현
public class SmartMachine implements PrinterDevice, CopyDevice, FaxDevice {
    @Override
    public void print() {
        System.out.println("print");
    }

    @Override
    public void copy() {
        System.out.println("copy");
    }

    @Override
    public void fax() {
        System.out.println("fax");
    }
}
```
필요한 인터페이스만 이용하여 구현을 해서 SmartMachine 을 해당 인터페이스로 업캐스팅하는 것입니다.  
그 후 필요에 따라 인터페이스 활용.

<br>

DIP - 의존관계 역전 원칙
---
> 추상화에 의존해야지, 구체화에 의존하면 안된다

- 고차원 모듈은 저차원 모듈에 의존하면 안 된다. 이 두 모듈 모두 다른 추상화된 것에 의존해야 한다.
- 추상화된 것은 구체적인 것에 의존하면 안 된다. 구체적인 것은 추상화된 것에 의존해야 한다.
- 자주 변경되는 구체(Concrete) 클래스에 의존하지 마라.

<br>

의존관계 역전 원칙 위반 예시
----

```java
public class ProductionService {

    private final LocalValidator localValidator;

    public ProductionService(LocalValidator localValidator) {
        this.localValidator = localValidator;
    }

    public void validate(Production production) {
        localValidator.validate(production);
    }

}

public class LocalValidator {

    public void validate(Production production) {
        //validate
    }
}
```
추가적인 Validator를 추가가 된다면
```java
//추가 적용
public class ETicketValidator {
    public void validate(Production production) {
        //validate
    }
}

// 서비스가 가지게되는 의존성이 늘어나게 된다
public class ProductionService {

    private final LocalValidator localValidator;
    private final ETicketValidator eTicketValidator;

    public ProductionService(LocalValidator localValidator, ETicketValidator eTicketValidator) {
        this.localValidator = localValidator;
        this.eTicketValidator = eTicketValidator;
    }

    public void validate(Production production) {
        if (production.getType().equals("L")) {
            localValidator.validate(production);
        } else if (production.getType().equals("E")) {
            eTicketValidator.validate(production);
        }

    }

}
```

의존관계 역전 원칙 준수 예시
---
```java
public interface Validator {
    void validate(Production production);
}

public class LocalValidator implements Validator {
    @Override
    public void validate(Production production) {
        //validate
    }
}

public class ETicketValidator implements Validator {
    @Override
    public void validate(Production production) {
        //validate
    }
}

public class ProductionService {

    private final Validator validator;

    public ProductionService(Validator validator) {
        this.validator = validator;
    }

    public void validate(Production production) {
        validator.validate(production);
    }
}
```
의존성이 역전됨으로써 ProductionService는 Validator의 구현체들로 부터 자유로워 졌고 코드의 변화도 없어지게 됩니다.
OCP에서 얻은 이점과 같이 확장에는 열리게 되고 변경에는 닫힌 구조가 완성이 될것입니다.

<br>

