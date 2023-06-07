MVC(Model-View-Controller)
===

> - Model : 애플리케이션의 비즈니스 로직과 데이터
> - View : 모델의 데이터를 시각적으로 표현하는 역할
> - Controller : 모델과 뷰를 연결하고 상호작용을 조정하는 역할
    

Model
---
데이터의 상태와 조작을 담당하며, 데이터베이스와의 상호작용, 데이터의 유효성 검사 등을 수행
Model은 독립적으로 존재하며, 데이터의 변경이나 업데이트를 처리하는 메소드를 제공
```java
public class User {
    private String name;
    private String email;
    
    // Getter and setter methods
    
    public void save() {
        // 데이터베이스에 사용자 정보 저장 로직
    }
    
    public void update() {
        // 데이터베이스에서 사용자 정보 업데이트 로직
    }
}
```
<br>

---

View
---
사용자 인터페이스(UI)를 구성하며, 데이터의 표시, 입력 양식, 사용자 이벤트 처리 등을 담당  
*View는 Model의 데이터를 읽기만
```java
public class UserView {
    public void displayUserInfo(String name, String email) {
        System.out.println("User Information:");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
    }
    
    public void showErrorMessage(String message) {
        System.out.println("Error: " + message);
    }
}
```
<br>

---

Controller
---
사용자의 입력을 받아 해당하는 Model 메소드를 호출하고, 그 결과를 View에 전달  
Controller는 사용자와의 상호작용을 처리하고, Model과 View 사이의 흐름을 제어
```java
public class UserController {
    private User model;
    private UserView view;

    public UserController(User model, UserView view) {
        this.model = model;
        this.view = view;
    }

    public void setUserName(String name) {
        model.setName(name);
    }

    public void setUserEmail(String email) {
        model.setEmail(email);
    }

    public void updateUserView() {
        view.displayUserInfo(model.getName(), model.getEmail());
    }

    public void saveUser() {
        try {
            model.save();
            view.displayUserInfo("User saved successfully.");
        } catch (Exception e) {
            view.showErrorMessage("Failed to save user.");
        }
    }
}
```

---

- Model은 User 클래스로 구현되며, 이름과 이메일을 저장하고 데이터베이스와 상호작용하는 메소드를 제공  
- View는 UserView 클래스로 구현되어 사용자 정보를 표시하고 오류 메시지를 출력  
- Controller는 UserController 클래스로 구현되어 사용자의 입력을 받고, Model과 View 간의 상호작용을 조정  

