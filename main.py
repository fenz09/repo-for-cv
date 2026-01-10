from flask import Flask, render_template, request, redirect, url_for, session
import pymysql
import pandas as pd
import matplotlib
matplotlib.use('Agg') 
import matplotlib.pyplot as plt
import io
import base64

app = Flask(__name__)
app.secret_key = 'ET'

def get_db_connection():
    return pymysql.connect(
    host="localhost", 
    user="root", 
    password="root", 
    database='expense_tracker'
)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/register', methods=['GET', 'POST'])
def register():
    if request.method == 'POST':
        userid = request.form['userid']
        username = request.form['username']
        email = request.form['email']
        password = request.form['password']
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        try:
            query = 'INSERT INTO users (user_id, username, email, password_hash) VALUES (%s, %s, %s, %s)'
            val = (userid, username, email, password)
            cursor.execute(query, val)
            conn.commit()
            return redirect(url_for('index'))
        except Exception as e:
            print(f"Error: {e}")
            return "Registration failed"
        finally:
            cursor.close()
            conn.close()
    
    return render_template('register.html')

@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        userid = request.form['userid']
        password = request.form['password']
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        try:
            query = 'SELECT * FROM users WHERE user_id = %s AND password_hash = %s'
            val = (userid, password)
            cursor.execute(query, val)
            user = cursor.fetchone()
            
            if user:
                session['user_id'] = userid
                session['username'] = user[1]
                return redirect(url_for('dashboard'))
            else:
                return render_template('login.html', error='Invalid credentials')
        except Exception as e:
            print(f"Error: {e}")
            return render_template('login.html', error='Login failed')
        finally:
            cursor.close()
            conn.close()
    
    return render_template('login.html')

@app.route('/dashboard', methods=['GET', 'POST'])
def dashboard():
    if 'user_id' not in session:
        return redirect(url_for('login'))
    return render_template('dashboard.html', username=session.get('username'))

@app.route('/add_expense', methods=['GET', 'POST'])
def add_expense():
    if 'user_id' not in session:
        return redirect(url_for('login'))
    
    if request.method == 'POST':
        amount = request.form['amount']
        category = request.form['category']
        expense_date = request.form['date']
        description = request.form.get('description', '')
        user_id = session.get('user_id')

        conn = get_db_connection()
        cursor = conn.cursor()
        try:
            query = 'INSERT INTO expenses (user_id, amount, category, expense_date, description) VALUES (%s, %s, %s, %s, %s)'
            val = (user_id, amount, category, expense_date, description)
            cursor.execute(query, val)
            conn.commit()
            return render_template('add_expense.html', success='Expense added successfully!')
        except Exception as e:
            print(f"Error: {e}")
            return render_template('add_expense.html', error='Failed to add expense')
        finally:
            cursor.close()
            conn.close()
    
    return render_template('add_expense.html')

@app.route('/remove_expense', methods=['GET', 'POST'])
def remove_expense():
    if 'user_id' not in session:
        return redirect(url_for('login'))
    user_id = session.get('user_id')
    conn = get_db_connection()
    cursor = conn.cursor()
    try:
        cursor.execute('SELECT expense_id FROM expenses WHERE user_id = %s ORDER BY expense_id', (user_id,))
        expense_ids = [row[0] for row in cursor.fetchall()]
    except Exception as e:
        print(f"Error fetching expense IDs: {e}")
        expense_ids = []
    finally:
        cursor.close()
        conn.close()
    
    if request.method == 'POST':
        expense_id = request.form['expense_id']

        conn = get_db_connection()
        cursor = conn.cursor()
        try:
            query = 'DELETE FROM expenses WHERE expense_id = %s AND user_id = %s'
            val = (expense_id, user_id)
            cursor.execute(query, val)
            conn.commit()
            return render_template('remove_expense.html', expense_ids=expense_ids, success='Expense removed successfully!')
        except Exception as e:
            print(f"Error: {e}")
            return render_template('remove_expense.html', expense_ids=expense_ids, error='Failed to remove expense')
        finally:
            cursor.close()
            conn.close()
    
    return render_template('remove_expense.html', expense_ids=expense_ids)

@app.route('/view_expenses', methods=['GET' , 'POST'])
def view_expenses():
    if 'user_id' not in session:
        return redirect(url_for('login'))
    
    user_id = session.get('user_id')
    conn = get_db_connection()
    cursor = conn.cursor()
    try:
        query = 'SELECT * FROM expenses WHERE user_id = %s'
        val = (user_id,)
        cursor.execute(query, val)
        expenses = cursor.fetchall()
        return render_template('view_expenses.html', expenses=expenses)
    except Exception as e:
        print(f"Error: {e}")
        return render_template('view_expenses.html', error='Failed to retrieve expenses')
    finally:
        cursor.close()
        conn.close()
        
@app.route('/update_expenses', methods=['GET', 'POST'])
def update_expenses():
    if 'user_id' not in session:
        return redirect(url_for('login'))
    
    user_id = session.get('user_id')
    conn = get_db_connection()
    cursor = conn.cursor()
    try:
        cursor.execute('SELECT expense_id FROM expenses WHERE user_id = %s ORDER BY expense_id', (user_id,))
        expense_ids = [row[0] for row in cursor.fetchall()]
    except Exception as e:
        print(f"Error fetching expense IDs: {e}")
        expense_ids = []
    finally:
        cursor.close()
        conn.close()
    
    if request.method == 'POST':
        expense_id = request.form['expense_id']
        amount = request.form['amount']
        category = request.form['category']
        expense_date = request.form['date']
        description = request.form.get('description', '')

        conn = get_db_connection()
        cursor = conn.cursor()
        try:
            query = '''UPDATE expenses 
                       SET amount = %s, category = %s, expense_date = %s, description = %s 
                       WHERE expense_id = %s AND user_id = %s'''
            val = (amount, category, expense_date, description, expense_id, user_id)
            cursor.execute(query, val)
            conn.commit()
            return render_template('update_expenses.html', expense_ids=expense_ids, success='Expense updated successfully!')
        except Exception as e:
            print(f"Error: {e}")
            return render_template('update_expenses.html', expense_ids=expense_ids, error='Failed to update expense')
        finally:
            cursor.close()
            conn.close()
    
    return render_template('update_expenses.html', expense_ids=expense_ids)

@app.route('/spending_by_category')
def spending_by_category():
    if 'user_id' not in session:
        return redirect(url_for('login')) 
    user_id = session.get('user_id')
    conn = get_db_connection()
    cursor = conn.cursor()
    
    try:
        cursor.execute('SELECT category, amount FROM expenses WHERE user_id = %s', (user_id,))
        data = cursor.fetchall()
        cursor.close()
        conn.close()
        
        if not data:
            return render_template('spending_by_category.html', plot_url=None)
        
        
        data = [(cat, float(amt)) for cat, amt in data]
        
        df = pd.DataFrame(data, columns=['Category', 'Amount'])
       
        category_totals = df.groupby('Category')['Amount'].sum()
    
        plt.figure(figsize=(10, 6))
        category_totals.plot(kind='bar', color='purple')
        plt.title('Total Spending by Category')
        plt.xlabel('Category')
        plt.ylabel('Amount ($)')
        plt.tight_layout()
        
        img = io.BytesIO()
        plt.savefig(img, format='png')
        img.seek(0)
        plot_url = base64.b64encode(img.getvalue()).decode()
        plt.close()
        
        return render_template('spending_by_category.html', plot_url=plot_url)
    except Exception as e:
        return render_template('spending_by_category.html', error=f'Failed to generate chart: {str(e)}')

@app.route('/monthly_trend')
def monthly_trend():
    if 'user_id' not in session:
        return redirect(url_for('login'))
    
    user_id = session.get('user_id')
    conn = get_db_connection()
    cursor = conn.cursor()
    
    try:
        cursor.execute('SELECT expense_date, amount FROM expenses WHERE user_id = %s', (user_id,))
        data = cursor.fetchall()
        cursor.close()
        conn.close()
        
        if not data:
            return render_template('monthly_trend.html', plot_url=None)
        
        data = [(date, float(amt)) for date, amt in data]
        
        df = pd.DataFrame(data, columns=['Date', 'Amount'])
        df['Date'] = pd.to_datetime(df['Date'])
        df['Month'] = df['Date'].dt.to_period('M')
        monthly_totals = df.groupby('Month')['Amount'].sum() 
        
        plt.figure(figsize=(10, 6))
        monthly_totals.plot(kind='line', marker='o', color='blue')
        plt.title('Monthly Spending Trend')
        plt.xlabel('Month')
        plt.ylabel('Total Amount ($)')
        plt.grid(True)
        plt.tight_layout()
        
        img = io.BytesIO()
        plt.savefig(img, format='png')
        img.seek(0)
        plot_url = base64.b64encode(img.getvalue()).decode()
        plt.close()
        
        return render_template('monthly_trend.html', plot_url=plot_url)
    except Exception as e:
        return render_template('monthly_trend.html', error=f'Failed to generate chart: {str(e)}')


@app.route('/get_total_spending')
def get_total_spending():
    if 'user_id' not in session:
        return {'total': 0}
    
    user_id = session.get('user_id')
    conn = get_db_connection()
    cursor = conn.cursor()
    
    cursor.execute('SELECT SUM(amount) FROM expenses WHERE user_id = %s', (user_id,))
    total = cursor.fetchone()[0] or 0
    cursor.close()
    conn.close()
    
    return {'total': float(total)}

@app.route('/logout')
def logout():
    session.clear()
    return redirect(url_for('index'))

if __name__ == '__main__':
    app.run(debug=True)