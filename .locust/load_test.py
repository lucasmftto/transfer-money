from locust import HttpUser, between, task


class WebsiteUser(HttpUser):
    wait_time = between(5, 15)
    
    @task
    def index(self):
        self.client.post("/transfer",
                         json={
                                "payer": "1",
                                "payee": "2",
                                "value": 1
                         })
        
