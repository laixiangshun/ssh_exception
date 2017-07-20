# ssh_exception
ssh系统统一异常处理，自定义filter，拦截器对权限，session过期等重定向操作
说明：1.ssh系统异常统一处理，url请求异常跳转到指定页面显示相关信息；ajax请求异常在error方法中显示信息；
			2.自定义filter对请求参数进行过滤，去掉ajax的get请求在cacahe=false时自动加上参数name="_"的过滤，不让其传递到action方法；装饰模式的理解；
			3.拦截器：对session过期，没有权限等分ajax请求和url请求进行相关提示和重定向操作
