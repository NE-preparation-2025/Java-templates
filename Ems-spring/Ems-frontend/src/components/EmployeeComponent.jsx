import React, { useEffect, useState } from 'react'
import { createEmployee, getEmployee, updateEmployee } from '../services/EmployeeService';
import { useNavigate,useParams } from 'react-router-dom';

function EmployeeComponent() {
    const [firstName,setFirstName]=useState('')
    const [lastName,setLastName]=useState('')
    const [email, setEmail]=useState('')
    const{id}=useParams();

    const  [errors, setErrors]= useState({
        firstName:'',
        lastName:'',
        email:''
    })

    useEffect(() =>{

        if(id){
            getEmployee(id).then((response ) => {
                setFirstName(response.data.firstName);                
                setLastName(response.data.lastName);
                setEmail(response.data.email);

            }).catch(error => {
                console.error(error)
            })
        }

    },[id])

    const navigator=useNavigate()

   
    
    function saveOrUpdateEmployee(e){
        e.preventDefault();
        if(validateForm()){
            const employee= {firstName,lastName,email}
            console.log(employee)
            if(id){
                updateEmployee(id, employee).then((response) => {
                    console.log(response.data);
                    navigator('/employees')
                }).catch(error =>{
                    console.error(error);
                })
                    
                

            }else{
                createEmployee(employee).then((response) => {
                    console.log(response.data);
                    navigator('/employees')
                }).catch(error =>{
                    console.error(error);
                })

            }        
          

        }
      
    }
    function validateForm(){
        let valid= true;
        const errorsCopy= {... errors}
        if(firstName.trim()){
errorsCopy.firstName=''
        }else {
            errorsCopy.firstName='FirstName is required'
            valid=false
        }
        if(lastName.trim()){
            errorsCopy.lastName='';

        }else{
            errorsCopy.lastName='last name required'
            valid=false
        }
        if(email.trim()){
            errorsCopy.email='';
        }else{
            errorsCopy.email='email required'
            valid=false
        }
        setErrors(errorsCopy);
        return valid;
    }

    function pageTitle(){
        if(id){
            return <h2 className='text-center'>Update Employee</h2>
        }else{
            return <h2 className='text-center'>Add Employee</h2>
        }

    }
  return (
    <div className='container'>
        <br />
        <div className='row'>
            <div className='card col-md-6 offset-md-3 offset-md-3' >
                {
                    pageTitle()
                }
                <div className='card-body'>
                    <form action="">
                        <div className='form-group-mb-2'>
                            <label htmlFor="" className='form-label'>First Name:</label>
                            <input type="text" name="firstName" id="" className={`form-control ${errors.firstName ? 'is-invalid' : ''}`} onChange={(e) => setFirstName(e.target.value)} placeholder='Enter Employee firstName' value={firstName}  />
                            {errors.firstName && <div  className='invalid-feedback'>{errors.firstName}</div> }
                        </div> 
                        <div className='form-group-mb-2'>
                            <label htmlFor="" className='form-label'>Last Name:</label>
                            
                            <input type="text" name="lastName" id="" className={`form-control ${errors.lastName ? 'is-invalid' : ''}`}onChange={(e) => setLastName(e.target.value)} placeholder='Enter Employee lastName' value={lastName}  />
                            {errors.lastName && <div  className='invalid-feedback'>{errors.lastName}</div> }
                        </div> 
                        <div className='form-group-mb-2'>
                            <label htmlFor="" className='form-label'>Email:</label>
                            <input type="email" name="email" id="" className={`form-control ${errors.email ? 'is-invalid' : ''}`} onChange={(e)=>setEmail(e.target.value)} placeholder='Enter Employee email' value={email}  />
                            {errors.email && <div  className='invalid-feedback'>{errors.email}</div> }
                        </div> 
                        <br />
                        <button className='btn btn-success' onClick={saveOrUpdateEmployee}>Submit</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
  )
}

export default EmployeeComponent