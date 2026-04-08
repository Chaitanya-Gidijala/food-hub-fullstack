// Stunning Admin Dashboard - Updated
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { OrderService } from '../../services/order.service';
import { RestaurantService } from '../../services/restaurant.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpClient } from '@angular/common/http';
import { API_ENDPOINTS } from '../../config/api.config';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  totalCustomers = 0;
  pendingApprovals = 0;
  totalOrders = 0;
  pendingRestaurants = 0;
  
  customers: any[] = [];
  restaurantOwners: any[] = [];
  orders: any[] = [];
  restaurants: any[] = [];
  
  showUsers = false;
  showRestaurantOwners = false;
  showOrders = false;
  showRestaurants = false;
  
  customerColumns = ['id', 'username', 'email', 'role'];
  ownerColumns = ['id', 'username', 'email', 'panCard', 'fssai', 'status', 'actions'];
  orderColumns = ['orderId', 'userId', 'restaurantName', 'totalPrice', 'status'];

  constructor(
    private authService: AuthService,
    private orderService: OrderService,
    private restaurantService: RestaurantService,
    private snackBar: MatSnackBar,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    console.log('Admin dashboard initializing...');
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    console.log('Loading dashboard data...');
    this.loadAllUsers();
    this.loadAllRestaurants();
    setTimeout(() => {
      this.loadAllOrders();
    }, 1000);
  }

  loadAllUsers(): void {
    console.log('Loading users from API...');
    this.http.get<any[]>(API_ENDPOINTS.USERS.GET_ALL).subscribe({
      next: (users) => {
        console.log('Users loaded:', users);
        this.customers = users.filter(u => u.role === 'CUSTOMER');
        this.restaurantOwners = users.filter(u => u.role === 'RESTAURANT_OWNER');
        this.totalCustomers = this.customers.length;
        this.pendingApprovals = this.restaurantOwners.filter(u => u.approvalStatus === 'PENDING').length;
        console.log('Customers:', this.customers);
        console.log('Restaurant owners:', this.restaurantOwners);
      },
      error: (err) => {
        console.error('Error loading users:', err);
        this.snackBar.open('Error loading users', 'Close', { duration: 3000 });
      }
    });
  }

  loadAllOrders(): void {
    // Get orders from all users
    let allOrders: any[] = [];
    let processedUsers = 0;
    
    if (this.customers.length === 0) {
      this.totalOrders = 0;
      return;
    }
    
    this.customers.forEach(customer => {
      this.http.get<any[]>(API_ENDPOINTS.ORDER.GET_BY_USER(customer.id)).subscribe({
        next: (orders) => {
          allOrders = allOrders.concat(orders);
          processedUsers++;
          if (processedUsers === this.customers.length) {
            this.orders = allOrders;
            this.totalOrders = allOrders.length;
            console.log('Total orders loaded:', this.totalOrders);
          }
        },
        error: (err) => {
          processedUsers++;
          if (processedUsers === this.customers.length) {
            this.orders = allOrders;
            this.totalOrders = allOrders.length;
          }
        }
      });
    });
  }

  viewUsers(): void {
    this.showUsers = !this.showUsers;
    this.showRestaurantOwners = false;
    this.showOrders = false;
    console.log('Customers section toggled:', this.showUsers);
  }

  viewRestaurantOwners(): void {
    this.showRestaurantOwners = !this.showRestaurantOwners;
    this.showUsers = false;
    this.showOrders = false;
    console.log('Restaurant owners section toggled:', this.showRestaurantOwners);
  }

  viewAllOrders(): void {
    this.showOrders = !this.showOrders;
    this.showUsers = false;
    this.showRestaurantOwners = false;
    if (this.showOrders && this.orders.length === 0) {
      this.loadAllOrders();
    }
  }

  approveUser(userId: number): void {
    this.http.put(API_ENDPOINTS.USERS.APPROVAL(userId), {}, {
      params: { status: 'APPROVED' }
    }).subscribe({
      next: () => {
        this.snackBar.open('User approved successfully', 'Close', { duration: 2000 });
        this.loadAllUsers();
      },
      error: (err) => {
        console.error('Approve error:', err);
        this.snackBar.open('Error approving user', 'Close', { duration: 3000 });
      }
    });
  }

  rejectUser(userId: number): void {
    this.http.put(API_ENDPOINTS.USERS.APPROVAL(userId), {}, {
      params: { status: 'REJECTED' }
    }).subscribe({
      next: () => {
        this.snackBar.open('User rejected', 'Close', { duration: 2000 });
        this.loadAllUsers();
      },
      error: (err) => {
        console.error('Reject error:', err);
        this.snackBar.open('Error rejecting user', 'Close', { duration: 3000 });
      }
    });
  }

  getStatusColor(status: string): string {
    const colors: any = {
      'PENDING': 'accent',
      'APPROVED': 'primary',
      'REJECTED': 'warn'
    };
    return colors[status] || 'primary';
  }

  getOrderStatusColor(status: string): string {
    const colors: any = {
      'CONFIRMED': 'primary',
      'PREPARING': 'accent',
      'DELIVERED': 'primary',
      'CANCELLED': 'warn'
    };
    return colors[status] || 'primary';
  }

  loadAllRestaurants(): void {
    this.restaurantService.getAllRestaurants().subscribe({
      next: (restaurants) => {
        this.restaurants = restaurants;
        this.pendingRestaurants = restaurants.filter(r => r.verificationStatus === 'PENDING').length;
      },
      error: () => this.snackBar.open('Error loading restaurants', 'Close', { duration: 3000 })
    });
  }

  viewRestaurants(): void {
    this.showRestaurants = !this.showRestaurants;
    this.showUsers = false;
    this.showRestaurantOwners = false;
    this.showOrders = false;
  }

  approveRestaurant(id: number): void {
    this.restaurantService.verifyRestaurant(id, 'APPROVED').subscribe({
      next: () => {
        this.snackBar.open('Restaurant approved', 'Close', { duration: 2000 });
        this.loadAllRestaurants();
      },
      error: () => this.snackBar.open('Error approving restaurant', 'Close', { duration: 3000 })
    });
  }

  rejectRestaurant(id: number): void {
    this.restaurantService.verifyRestaurant(id, 'REJECTED').subscribe({
      next: () => {
        this.snackBar.open('Restaurant rejected', 'Close', { duration: 2000 });
        this.loadAllRestaurants();
      },
      error: () => this.snackBar.open('Error rejecting restaurant', 'Close', { duration: 3000 })
    });
  }
}
