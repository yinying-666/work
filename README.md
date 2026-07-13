# work
小组实训作业
hhh上传文件总结：CodeX 生成 Coze AI 客服对接接口
AiController.java
项目 controller 包下的 AI 问答接口代码，实现 POST /ai/chat 接口，对接扣子 Coze AI 知识库，接收前端 question 提问，返回简洁客服回答。
ExpressSystemApplication.java（项目启动类）
新增@Bean方法注入RestTemplate，为 AI 接口提供远程 HTTP 调用能力。
