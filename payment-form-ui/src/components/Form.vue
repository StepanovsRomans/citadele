<template>
    <v-content>
        <v-row justify="center">
            <v-col cols="12" sm="8" md="6">
                <v-alert
                        type="success"
                        v-if="showSuccess"
                        :dismissible="true">
                    Data successfully sent
                </v-alert>
                <v-alert
                        type="error"
                        v-if="showFailed"
                        :dismissible="true">
                    {{ failedMessage }}
                </v-alert>
                <v-card>
                    <v-toolbar color="red lighten-1" dark>

                        <v-toolbar-title>Payment Form</v-toolbar-title>

                        <div class="flex-grow-1"></div>
                    </v-toolbar>

                    <form v-model="valid"
                          lazy-validation>

                        <v-text-field
                                v-model="clientNumber"
                                label="Client Number"
                                required
                                :rules="clientNumberValidationRules"
                                :counter="5"
                        ></v-text-field>
                        <v-subheader>Remitter</v-subheader>
                        <v-text-field
                                v-model="remitterName"
                                label="Name,surname / Company name"
                                required
                                :rules="nameValidationRules"
                                :counter="30"
                        ></v-text-field>
                        <v-text-field
                                v-model="remitterPersonalId"
                                label="Personas kods / Reģistrācijas Nr."
                                required
                                :rules="personalIdValidationRules"
                                :counter="15"
                        ></v-text-field>
                        <v-text-field
                                v-model="remitterAccountNo"
                                label="Account No"
                                required
                                :rules="accountNoValidationRules"
                                :counter="15"
                        ></v-text-field>
                        <v-subheader>Beneficiary</v-subheader>
                        <v-text-field
                                v-model="beneficiaryName"
                                label="Name,surname / Company name"
                                required
                                :rules="nameValidationRules"
                                :counter="30"
                        ></v-text-field>
                        <v-text-field
                                v-model="beneficiaryPersonalId"
                                label="Personas kods / Reģistrācijas Nr."
                                required
                                :rules="personalIdValidationRules"
                                :counter="15"
                        ></v-text-field>
                        <v-text-field
                                v-model="beneficiaryAccountNo"
                                label="Account No"
                                required
                                :rules="accountNoValidationRules"
                                :counter="15"
                        ></v-text-field>
                        <v-text-field
                                v-model="beneficiaryResidenceCountry"
                                label="Residence country"
                                required
                                :rules="countryValidationRules"
                                :counter="20"
                        ></v-text-field>
                        <v-text-field
                                v-model="beneficiaryBank"
                                label="Beneficiary bank"
                                required
                                :rules="bankValidationRules"
                                :counter="40"
                        ></v-text-field>
                        <v-text-field
                                v-model="beneficiaryBankCode"
                                label="Bank code"
                                required
                                :rules="bankCodeValidationRules"
                                :counter="10"
                        ></v-text-field>

                        <v-subheader>Payment information</v-subheader>

                        <v-text-field
                                v-model="amount"
                                label="Amount in figures and currency"
                                required
                                :rules="amountValidationRules"
                                :counter="15"
                        ></v-text-field>
                        <v-text-field
                                v-model="bankFee"
                                label="Bank fee"
                                required
                                :rules="bankFeeValidationRules"
                                :counter="15"
                        ></v-text-field>
                        <v-text-field
                                v-model="amountInWords"
                                label="Amount in words"
                                required
                                :rules="amountInWordsValidationRules"
                        ></v-text-field>
                        <v-select
                                :items="paymentTypes"
                                v-model="paymentType"
                                label="Payment Type"
                        ></v-select>
                        <v-text-field
                                v-model="exchangeRate"
                                label="Exchange rate"
                                required
                                :rules="exchangeRateValidationRules"
                                :counter="10"
                        ></v-text-field>
                        <v-menu
                                ref="menu"
                                v-model="menu"
                                :close-on-content-click="false"
                                :return-value.sync="valueDate"
                                transition="scale-transition"
                                offset-y
                                full-width
                                min-width="290px"
                        >
                            <template v-slot:activator="{ on }">
                                <v-text-field
                                        v-model="valueDate"
                                        label="Value Date"
                                        readonly
                                        v-on="on"
                                ></v-text-field>
                            </template>
                            <v-date-picker :rules="valueDateValidationRules" v-model="valueDate" no-title scrollable>
                                <div class="flex-grow-1"></div>
                                <v-btn text color="primary" @click="menu = false">Cancel</v-btn>
                                <v-btn text color="primary" @click="$refs.menu.save(valueDate)">OK</v-btn>
                            </v-date-picker>
                        </v-menu>
                        <v-text-field
                                v-model="codeOfExternalPayment"
                                label="Code of external payment"
                                required
                                :rules="codeOfExternalPaymentValidationRules"
                                :counter="4"
                        ></v-text-field>
                        <v-text-field
                                v-model="paymentDetails"
                                label="Payment details"
                                required
                                :rules="paymentDetailsValidationRules"
                                :counter="144"
                        ></v-text-field>
                        <v-btn class="mr-4" @click="onSubmit" :disabled="!valid">submit  <v-progress-circular
                                indeterminate
                                color="green"
                                v-if="showCircular"
                        ></v-progress-circular></v-btn>
                        <v-btn @click="onClear">clear</v-btn>
                    </form>

                </v-card>
            </v-col>
        </v-row>
    </v-content>
</template>

<script lang="ts">
import {Component, Vue} from 'vue-property-decorator';
import axios from 'axios';

@Component({
  components: {},
})

export default class Form extends Vue {
    private clientNumber: string = '';
    private date: Date | undefined;

    private remitterName: string = '';
    private remitterPersonalId: string = '';
    private remitterAccountNo: string = '';

    private beneficiaryName: string = '';
    private beneficiaryPersonalId: string = '';
    private beneficiaryAccountNo: string = '';
    private beneficiaryResidenceCountry: string = '';
    private beneficiaryBank: string = '';
    private beneficiaryBankCode: string = '';

    private amount: string = '';
    private bankFee: string = '';
    private amountInWords: string = '';
    private paymentType: string = '';
    private exchangeRate: string = '';
    private valueDate: Date = null;
    private codeOfExternalPayment: string = '';
    private paymentDetails: string = '';

    private paymentTypes = [
        'Cash',
        'Cheque',
        'Debit',
        'Credit',
    ];
    private menu = false;
    private showSuccess = false;
    private showFailed = false;
    private showCircular = false;
    private failedMessage = '';

    private valid: boolean = true;

    private clientNumberValidationRules = [
        (v: any) => !!v || 'Client Number is required',
        (v: any) => v.length <= 5 || 'Client Number should be 5 characters or less',
        (v: any) => /^\d+$/.test(v) || 'Client Number should contain only numbers',
    ];

    private nameValidationRules = [
        (v: any) => !!v || 'Name is required',
        (v: any) => v.length <= 30 || 'Name should be 30 characters or less',
    ];

    private personalIdValidationRules = [
        (v: any) => !!v || 'Personal Id is required',
        (v: any) => v.length <= 15 || 'Personal Id should be 15 characters or less',
    ];

    private accountNoValidationRules = [
        (v: any) => !!v || 'Account No is required',
        (v: any) => v.length <= 15 || 'Account No should be 15 characters or less',
    ];

    private countryValidationRules = [
        (v: any) => !!v || 'Country is required',
        (v: any) => v.length <= 20 || 'Country should be 20 characters or less',
    ];

    private bankValidationRules = [
        (v: any) => !!v || 'Bank is required',
        (v: any) => v.length <= 40 || 'Bank should be 40 characters or less',
    ];

    private bankCodeValidationRules = [
        (v: any) => !!v || 'Bank Code is required',
        (v: any) => v.length <= 10 || 'Bank Code should be 10 characters or less',
    ];

    private amountValidationRules = [
        (v: any) => !!v || 'Amount is required',
        (v: any) => v.length <= 15 || 'Amount should be 15 characters or less',
        (v: any) => /^\d+/.test(v) || 'Amount should start with numbers',
    ];

    private bankFeeValidationRules = [
        (v: any) => !!v || 'Bank Fee is required',
        (v: any) => v.length <= 15 || 'Bank Fee should be 15 characters or less',
    ];

    private amountInWordsValidationRules = [
        (v: any) => !!v || 'Amount In Words is required',
        (v: any) => /^[a-zA-Z\s]+$/.test(v) || 'Amount in Words should contain only letters',
    ];

    private paymentTypeValidationRules = [
        (v: any) => !!v || 'Payment Type is required',
    ];

    private exchangeRateValidationRules = [
        (v: any) => !!v || 'Exchange Rate is required',
        (v: any) => v.length <= 10 || 'Exchange Rate should be 10 characters or less',
    ];

    private valueDateValidationRules = [
        (v: any) => !!v || 'Value Date is required',
    ];

    private codeOfExternalPaymentValidationRules = [
        (v: any) => !!v || 'Code Of External Payment is required',
        (v: any) => v.length === 4 || 'Code Of External Payment should be 4 characters',
        (v: any) => /^[a-zA-Z]+$/.test(v) || 'Code Of External Payment should contain only letters',
    ];

    private paymentDetailsValidationRules = [
        (v: any) => !!v || 'Payment Details is required',
        (v: any) => v.length <= 144 || 'Payment Details should be 144 characters or less',
    ];

    private onSubmit() {
        this.date = new Date();

        this.sendForm();
    }

    private scrollToTop() {
        window.scrollTo(0, 0);
    }

    private sendForm() {
        this.showCircular = true;
        axios.post('http://localhost:9090/payment', {
            clientNumber: this.clientNumber,
            date: this.date,
            remitterName: this.remitterName,
            remitterPersonalId: this.remitterPersonalId,
            remitterAccountNo: this.remitterAccountNo,
            beneficiaryName: this.beneficiaryName,
            beneficiaryPersonalId: this.beneficiaryPersonalId,
            beneficiaryAccountNo: this.beneficiaryAccountNo,
            beneficiaryResidenceCountry: this.beneficiaryResidenceCountry,
            beneficiaryBank: this.beneficiaryBank,
            beneficiaryBankCode: this.beneficiaryBankCode,
            amount: this.amount,
            bankFee: this.bankFee,
            amountInWords: this.amountInWords,
            paymentType: this.paymentType,
            exchangeRate: this.exchangeRate,
            valueDate: this.valueDate,
            codeOfExternalPayment: this.codeOfExternalPayment,
            paymentDetails: this.paymentDetails })
            .then(() => {
                this.showSuccess = true;

                this.showCircular = false;
                this.scrollToTop();
            })
            .catch((error) => {
                this.failedMessage = error;
                this.showFailed = true;

                this.showCircular = false;
                this.scrollToTop();
            });
    }

    private  onClear() {
        this.clientNumber = '';
        this.date = undefined;

        this.remitterName = '';
        this.remitterPersonalId = '';
        this.remitterAccountNo = '';

        this.beneficiaryName = '';
        this.beneficiaryPersonalId = '';
        this.beneficiaryAccountNo = '';
        this.beneficiaryResidenceCountry = '';
        this.beneficiaryBank = '';
        this.beneficiaryBankCode = '';

        this.amount = '';
        this.bankFee = '';
        this.amountInWords = '';
        this.paymentType = '';
        this.exchangeRate = '';
        this.valueDate = undefined;
        this.codeOfExternalPayment = '';
        this.paymentDetails = '';
    }
}
</script>
