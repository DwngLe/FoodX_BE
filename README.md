# Website đánh giá nhà hàng: FoodX
Một trang web xếp hạng các nhà hàng dựa trên đánh giá của người dùng, cung cấp thông tin về vị trí nhà hàng, thông tin liên hệ, giờ mở cửa, giá cả và các đánh giá của người dùng. Website còn cho phép: thêm nhà hàng, xác thực là chủ nhà hàng và quản lý nhà hàng


## Authors

- [@DwngLee - Back-end](https://github.com/DwngLee)
- [@Semicof - Front-end](https://github.com/Semicof)


## Deployment
## ***Server***


**Bước 1: Clone dự án ở repo**

```bash
  git clone https://github.com/DwngLe/FoodX_BE
```
**Bước 2: Setup Maven (có thể bỏ qua nếu đã cài đặt Maven)**
- Cài đặt maven: ` https://maven.apache.org/download.cgi`
- Giải nén file và lưu vào thư mục
- Set MAVEN_HOME vào `System variable`
![ibo5A](https://github.com/DwngLee/Project-IOT/assets/156188368/8aadced8-9f5c-4a09-9007-b2ddc2b909ff)
- Set path cho maven
![wl0eU](https://github.com/DwngLee/Project-IOT/assets/156188368/e9850992-7115-4b4e-a9cf-e297ab476fd3)
- Thêm maven plugin vào file POM.XML
```bash
   <build>
       <plugins>
         <plugin>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-maven-plugin</artifactId>
          </plugin>
        </plugins>
      </build>
```
**Bước 3: Kết nối tới hệ quản trị cơ sở dữ liệu MySQL**
- Chỉnh sửa các thông tin tới database của bạn: url, username, password, driver
![z5275458207180_c80c7ad668658578d771bd71fcbbf30b](https://github.com/DwngLee/Project-IOT/assets/156188368/3d9eaea9-8cc3-4e8d-80b6-b83f0e79f7d1)


**Bước 4: Build Spring Boot Project bằng Maven**
```bash
  mvn package
```
hoặc
```bash
  mvn install / mvn clean install
```
**Bước 5: Chạy ứng dụng Spring Boot sử dụng Maven**
```bash
  mvn spring-boot:run
```




## API Reference

- API docs: https://foodxbe-production.up.railway.app/api/swagger-ui/index.html

