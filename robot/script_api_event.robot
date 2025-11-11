*** Settings ***
Library         RequestsLibrary
Library         Collections
Library         DateTime
Library         FakerLibrary
Library         String
Library         OperatingSystem
Resource        resource_api_event.robot

*** Keywords ***
# --- SETUP E SESSÃO --- (EXISTENTE)
Criar Sessao API
    [Documentation]     Cria uma sessao HTTP persistente.
    Create Session      api_session     ${BASE_URL}     verify=True

# --- KEYWORDS EXISTENTES DO cases_post_event.robot (MANTIDAS) ---
Executar POST e Validar Status
    [Documentation]     Executa POST com payload e valida status code.
    [Arguments]         ${payload}      ${expected_status}
    
    ${headers}=         Create Dictionary       Content-Type=application/json
    ${response}=        POST On Session     api_session     ${ENDPOINT_EVENT}
    ...             json=${payload} 
    ...             headers=${headers}
    ...             expected_status=any

    Should Be Equal As Integers      ${response.status_code}     ${expected_status}
    RETURN      ${response}

Validar Response Sucesso 201
    [Arguments]         ${response}     ${expected_name}
    [Documentation]     Valida o conteudo da resposta 201.
    ${response_json}=   Set Variable    ${response.json()}
    Should Not Be Empty     ${response_json['id']}
    Should Not Be Empty     ${response_json['createdAt']}
    Should Not Be Empty     ${response_json['updatedAt']}
    
    # Validacoes de dados de sucesso
    Should Be Equal     ${response_json['name']}    ${expected_name}
    Should Be Equal As Integers      ${response_json['capacity']}     ${SUCCESS_CAPACITY}
    
Validar Mensagem de Erro
    [Arguments]         ${response}     ${partial_message}
    [Documentation]     Verifica se a resposta de erro contem a mensagem esperada.
    ${response_text}=   Set Variable    ${response.text}
    Should Contain      ${response_text}    ${partial_message}

Gerar Nome Evento Aleatorio
    [Documentation]     Gera um nome de evento unico e aleatorio
    ${random_word}=     FakerLibrary.Word
    ${timestamp}=       Get Current Date    result_format=%H%M%S
    ${event_name}=      Set Variable    Evento ${random_word} ${timestamp}
    RETURN      ${event_name}

Gerar Payload Sucesso Completo Aleatório
    [Documentation]     Gera um payload com todos os campos validos.
    ${event_name}=      Gerar Nome Evento Aleatorio
    &{payload}=     Create Dictionary
    ...             name=${event_name}
    ...             description=Descricao completa do evento.
    ...             location=${SUCCESS_LOCATION}
    ...             capacity=${SUCCESS_CAPACITY}
    ...             ageRestriction=18
    ...             dateInitial=${FUTURE_DATE_INITIAL}
    ...             dateFinal=${FUTURE_DATE_FINAL}
    RETURN      ${payload}

Gerar Payload Sucesso Completo 
    [Documentation]     Gera um payload com todos os campos validos.
    &{payload}=     Create Dictionary
    ...             name=teste
    ...             description=Descricao completa do evento.
    ...             location=${SUCCESS_LOCATION}
    ...             capacity=${SUCCESS_CAPACITY}
    ...             ageRestriction=18
    ...             dateInitial=${FUTURE_DATE_INITIAL}
    ...             dateFinal=${FUTURE_DATE_FINAL}
    RETURN      ${payload}

Gerar Payload Sucesso Minimo
    [Documentation]     Gera um payload apenas com campos obrigatorios validos.
    ${event_name}=      Gerar Nome Evento Aleatorio
    &{payload}=     Create Dictionary
    ...             name=${event_name}
    ...             location=${SUCCESS_LOCATION}
    ...             capacity=${SUCCESS_CAPACITY}
    ...             dateInitial=${FUTURE_DATE_INITIAL}
    ...             dateFinal=${FUTURE_DATE_FINAL}
    RETURN      ${payload}

Gerar Payload Datas Iguais
    [Documentation]     Gera um payload onde data inicial e igual a final.
    ${event_name}=      Gerar Nome Evento Aleatorio
    &{payload}=     Create Dictionary
    ...             name=${event_name}
    ...             location=${SUCCESS_LOCATION}
    ...             capacity=${SUCCESS_CAPACITY}
    ...             dateInitial=${FUTURE_DATE_INITIAL}
    ...             dateFinal=${FUTURE_DATE_INITIAL}
    RETURN      ${payload}

Gerar Payload Campo Ausente
    [Arguments]         ${missing_field}
    [Documentation]     Remove um campo obrigatorio do payload de sucesso minimo.
    ${event_name}=      Gerar Nome Evento Aleatorio
    &{payload}=     Create Dictionary
    ...             name=${event_name}
    ...             location=${SUCCESS_LOCATION}
    ...             capacity=${SUCCESS_CAPACITY}
    ...             dateInitial=${FUTURE_DATE_INITIAL}
    ...             dateFinal=${FUTURE_DATE_FINAL}
    Remove From Dictionary      ${payload}      ${missing_field}
    RETURN      ${payload}

Gerar Payload Campo Negativo
    [Arguments]         ${field_name}       ${field_value}
    [Documentation]     Define um valor invalido (negativo) para um campo numerico.
    &{payload}=     Gerar Payload Sucesso Minimo
    Set To Dictionary       ${payload}      ${field_name}=${field_value}
    RETURN      ${payload}

Gerar Payload Data Passada
    [Documentation]     Define dateInitial como uma data passada.
    ${past_date}=       Convert Date    2020-01-01T10:00:00     result_format=%Y-%m-%dT%H:%M:%S
    &{payload}=     Gerar Payload Sucesso Minimo
    Set To Dictionary       ${payload}      dateInitial=${past_date}
    RETURN      ${payload}

Gerar Payload Data Final Anterior
    [Documentation]     Define dateFinal anterior a dateInitial.
    &{payload}=     Gerar Payload Sucesso Minimo
    Set To Dictionary       ${payload}      dateInitial=2030-10-10T12:00:00
    Set To Dictionary       ${payload}      dateFinal=2030-10-10T11:00:00
    RETURN      ${payload}

Gerar Payload Formato Data Invalido
    [Documentation]     Define um formato de data invalido.
    &{payload}=     Gerar Payload Sucesso Minimo
    Set To Dictionary       ${payload}      dateInitial=10/10/2030 18:00
    RETURN      ${payload}

Gerar Payload Tipo Invalido
    [Arguments]         ${field_name}       ${invalid_value}
    [Documentation]     Define um tipo de dado invalido (ex: int em vez de string).
    &{payload}=     Gerar Payload Sucesso Minimo
    Set To Dictionary       ${payload}      ${field_name}=${invalid_value}
    RETURN      ${payload}

# --- NOVAS KEYWORDS PARA TICKETS (ADICIONADAS) ---
Criar Evento Base Para Tickets
    [Documentation]     Cria um evento específico para testes de ticket
    &{event_payload}=   Gerar Payload Evento Para Tickets
    ${headers}=         Create Dictionary    Content-Type=application/json
    
    ${response}=        POST On Session     api_session     ${ENDPOINT_EVENT}
    ...                 json=${event_payload}
    ...                 headers=${headers}
    ...                 expected_status=any
    
    Should Be Equal As Integers      ${response.status_code}     201
    ${event_id}=        Set Variable    ${response.json()['id']}
    Should Not Be Empty    ${event_id}
    RETURN              ${event_id}

Gerar Payload Evento Para Tickets
    [Documentation]     Gera payload específico para testes de ticket
    ${event_name}=      Gerar Nome Evento Para Tickets
    &{payload}=     Create Dictionary
    ...             name=${event_name}
    ...             location=${SUCCESS_LOCATION}
    ...             capacity=${SUCCESS_CAPACITY}
    ...             dateInitial=${FUTURE_DATE_INITIAL}
    ...             dateFinal=${FUTURE_DATE_FINAL}
    RETURN      ${payload}

Gerar Nome Evento Para Tickets
    [Documentation]     Gera nome único para eventos de ticket
    ${random_word}=     FakerLibrary.Word
    ${timestamp}=       Get Current Date    result_format=%Y%m%d%H%M%S
    ${event_name}=      Set Variable    Evento-Ticket-${random_word}-${timestamp}
    RETURN              ${event_name}

Executar Post Ticket Para Evento
    [Arguments]    ${event_id}    ${payload}    ${expected_status}
    
    Should Not Be Equal    ${event_id}    ${None}
    Should Not Be Empty    ${event_id}
    
    ${url_final}=   Replace String    ${ENDPOINT_TICKET}  {eventId}  ${event_id}
    ${headers}=     Create Dictionary   Content-Type=application/json
    
    ${response}=    POST On Session     api_session     ${url_final}
    ...             json=${payload} 
    ...             headers=${headers}
    ...             expected_status=any
    
    Should Be Equal As Integers      ${response.status_code}     ${expected_status}
    RETURN      ${response}

Gerar Nome Ticket Aleatorio
    [Documentation]     Gera nome único para ticket
    ${random_word}=     FakerLibrary.Word
    ${timestamp}=       Get Current Date    result_format=%H%M%S
    ${ticket_name}=     Set Variable    Ticket-${random_word}-${timestamp}
    RETURN      ${ticket_name}

Criar Payload Ticket Basico
    [Arguments]    ${nome}    ${valor}    ${capacidade}
    [Documentation]    Gera payload básico para ticket
    &{payload}=     Create Dictionary
    ...             name=${nome}
    ...             description=Ingresso de Teste ${nome}
    ...             price=${valor}
    ...             capacity=${capacidade}
    ...             dateInitial=${FUTURE_DATE_INITIAL}
    ...             dateFinal=${FUTURE_DATE_FINAL}
    RETURN      ${payload}

Criar Payload Ticket Com Datas
    [Arguments]    ${nome}    ${valor}    ${capacidade}    ${data_inicial}    ${data_final}
    [Documentation]    Gera payload para ticket com datas customizadas
    &{payload}=     Create Dictionary
    ...             name=${nome}
    ...             description=Ingresso de Teste ${nome}
    ...             price=${valor}
    ...             capacity=${capacidade}
    ...             dateInitial=${data_inicial}
    ...             dateFinal=${data_final}
    RETURN      ${payload}

Validar Response Ticket Sucesso 201
    [Arguments]     ${response}     ${nome_esperado}    ${event_id_esperado}
    [Documentation]     Valida resposta 201 do Ticket
    ${response_json}=   Set Variable    ${response.json()}
    Should Not Be Empty     ${response_json['id']}
    Should Be Equal     ${response_json['name']}    ${nome_esperado}
    Should Be Equal     ${response_json['event']}    ${event_id_esperado}

Validar Mensagem de Erro Ticket
    [Arguments]         ${response}     ${mensagem_parcial}
    [Documentation]     Verifica se resposta de erro contém mensagem esperada
    ${response_text}=   Set Variable    ${response.text}
    Should Contain      ${response_text}    ${mensagem_parcial}

# --- KEYWORDS ADICIONAIS PARA CENÁRIOS DE COMPRA ---
Criar Payload Compra Ticket
    [Arguments]    ${ticket_type_id}    ${quantidade_tickets}=1
    [Documentation]    Cria payload CORRETO para compra de ticket
    
    ${random_suffix}=    Generate Random String    10    [NUMBERS]
    
    # Criar holders
    @{holders_list}=    Create List
    FOR    ${index}    IN RANGE    ${quantidade_tickets}
        ${holder_name}=     FakerLibrary.Name
        ${holder_email}=    FakerLibrary.Email
        ${anos_aleatorios}=    Evaluate    random.randint(18, 65)
        ${data_atual}=      Get Current Date
        ${data_nascimento}=    Subtract Time From Date    ${data_atual}    ${anos_aleatorios * 365} days
        ${holder_birth_date}=    Convert Date    ${data_nascimento}    result_format=%Y-%m-%d
        
        &{holder}=    Create Dictionary
        ...    name=${holder_name}
        ...    email=${holder_email}
        ...    birthDate=${holder_birth_date}
        
        Append To List    ${holders_list}    ${holder}
    END
    
    # Criar payer
    &{payer}=    Create Dictionary
    ...    name=Comprador ${random_suffix}
    ...    email=comprador${random_suffix}@test.com
    
    # Criar ticket
    &{ticket}=    Create Dictionary
    ...    id=${ticket_type_id}
    ...    holders=${holders_list}
    
    # Criar payload FINAL
    @{tickets_list}=    Create List    ${ticket}
    
    &{payload}=    Create Dictionary
    ...    tickets=${tickets_list}
    ...    payer=${payer}
    
    RETURN    ${payload}   

Logar Response Para Debug
    [Arguments]    ${response}
    [Documentation]    Loga a resposta completa para debugging
    
    Log    \n=== RESPONSE DEBUG ===    console=True
    Log    Status Code: ${response.status_code}    console=True
    Log    Response Body: ${response.text}    console=True
    Log    === FIM RESPONSE ===    console=True

Criar Payload Compra Multiplos Tickets
    [Arguments]    ${ticket_type_id}    ${quantidade}
    [Documentation]    Cria payload com múltiplos tickets do mesmo tipo
    
    ${random_suffix}=    Generate Random String    10    [NUMBERS]
    ${nome_pagador}=     Set Variable    TESTUSER${random_suffix}
    ${email_pagador}=    Set Variable    test_user_${random_suffix}@test.com
    
    @{holders}=          Create List
    FOR    ${index}    IN RANGE    ${quantidade}
        ${holder_name}=     FakerLibrary.Name
        ${holder_email}=    FakerLibrary.Email
        # CORREÇÃO: Gerar data de nascimento manualmente
        ${anos_aleatorios}=    Evaluate    random.randint(18, 65)
        ${data_atual}=      Get Current Date
        ${data_nascimento}=    Subtract Time From Date    ${data_atual}    ${anos_aleatorios * 365} days
        ${holder_birth_date}=    Convert Date    ${data_nascimento}    result_format=%Y-%m-%d
        
        &{holder}=          Create Dictionary
        ...                 name=${holder_name}
        ...                 email=${holder_email}
        ...                 birthDate=${holder_birth_date}
        
        Append To List      ${holders}    ${holder}
    END
    
    &{payer}=           Create Dictionary
    ...                 name=${nome_pagador}
    ...                 email=${email_pagador}
    
    &{ticket}=    Create Dictionary
...           id=${ticket_type_id}
...           holders=[${holder}] 
    
    &{payload}=    Create Dictionary
...            tickets=[${ticket}]
...            payer=${payer}
    
    RETURN    ${payload}

# --- KEYWORDS PARA CENÁRIOS NEGATIVOS DE COMPRA ---
Criar Payload Compra Ticket Sem Tickets
    [Arguments]    ${ticket_type_id}
    [Documentation]    Cria payload sem o campo tickets
    
    ${random_suffix}=    Generate Random String    10    [NUMBERS]
    ${nome_pagador}=     Set Variable    TESTUSER${random_suffix}
    ${email_pagador}=    Set Variable    test_user_${random_suffix}@test.com
    
    &{payer}=           Create Dictionary
    ...                 name=${nome_pagador}
    ...                 email=${email_pagador}
    
    &{payload}=         Create Dictionary
    ...                 payer=${payer}
    # Campo tickets omitido propositalmente
    
    RETURN    ${payload}

Criar Payload Compra Ticket Sem Payer
    [Arguments]    ${ticket_type_id}
    [Documentation]    Cria payload sem o campo payer
    
    ${holder_name}=     FakerLibrary.Name
    ${holder_email}=    FakerLibrary.Email
    # CORREÇÃO: Gerar data de nascimento manualmente
    ${anos_aleatorios}=    Evaluate    random.randint(18, 65)
    ${data_atual}=      Get Current Date
    ${data_nascimento}=    Subtract Time From Date    ${data_atual}    ${anos_aleatorios * 365} days
    ${holder_birth_date}=    Convert Date    ${data_nascimento}    result_format=%Y-%m-%d
    
    &{holder}=          Create Dictionary
    ...                 name=${holder_name}
    ...                 email=${holder_email}
    ...                 birthDate=${holder_birth_date}
    
    &{ticket}=    Create Dictionary
...           id=${ticket_type_id}
...           holders=[${holder}] 
    
    &{payload}=    Create Dictionary
...            tickets=[${ticket}]
...            payer=
    # Campo payer omitido propositalmente
    
    RETURN    ${payload}

Criar Payload Compra Ticket Email Invalido
    [Arguments]    ${ticket_type_id}
    [Documentation]    Cria payload com email do payer inválido
    
    ${holder_name}=     FakerLibrary.Name
    ${holder_email}=    FakerLibrary.Email
    # CORREÇÃO: Gerar data de nascimento manualmente
    ${anos_aleatorios}=    Evaluate    random.randint(18, 65)
    ${data_atual}=      Get Current Date
    ${data_nascimento}=    Subtract Time From Date    ${data_atual}    ${anos_aleatorios * 365} days
    ${holder_birth_date}=    Convert Date    ${data_nascimento}    result_format=%Y-%m-%d
    
    &{holder}=          Create Dictionary
    ...                 name=${holder_name}
    ...                 email=${holder_email}
    ...                 birthDate=${holder_birth_date}
    
    &{payer}=           Create Dictionary
    ...                 name=Test User
    ...                 email=email_invalido    # Email sem @ e domínio
    
    &{ticket}=    Create Dictionary
...           id=${ticket_type_id}
...           holders=[${holder}] 
    
    &{payload}=    Create Dictionary
...            tickets=[${ticket}]
...            payer=${payer}
    
    RETURN    ${payload}

Criar Payload Compra Ticket Com Titular Fixo
    [Arguments]    ${ticket_type_id}
    [Documentation]    Cria payload com titular fixo para testes de duplicação
    
    ${random_suffix}=    Generate Random String    10    [NUMBERS]
    ${nome_pagador}=     Set Variable    TESTUSER${random_suffix}
    ${email_pagador}=    Set Variable    test_user_${random_suffix}@test.com
    
    # Titular fixo para garantir duplicação
    &{holder}=          Create Dictionary
    ...                 name=João Silva
    ...                 email=joao.silva@example.com
    ...                 birthDate=1990-05-15
    
    &{payer}=           Create Dictionary
    ...                 name=${nome_pagador}
    ...                 email=${email_pagador}
    
    &{ticket}=    Create Dictionary
...           id=${ticket_type_id}
...           holders=[${holder}] 
    
    &{payload}=    Create Dictionary
...            tickets=[${ticket}]
...            payer=${payer}
    
    RETURN    ${payload}

Criar Payload Compra Com Titular Duplicado
    [Arguments]    ${ticket_type_id}
    [Documentation]    Cria payload com mesmo titular duplicado CORRETAMENTE
    
    ${random_suffix}=    Generate Random String    10    [NUMBERS]
    ${nome_pagador}=     Set Variable    TESTUSER${random_suffix}
    ${email_pagador}=    Set Variable    test_user_${random_suffix}@test.com
    
    # Criar o holder uma vez
    &{holder}=          Create Dictionary
    ...                 name=Maria Santos
    ...                 email=maria.santos@example.com
    ...                 birthDate=1985-08-20
    
    # Criar uma lista com o mesmo holder DUAS VEZES
    @{holders_list}=    Create List    ${holder}    ${holder}
    
    &{payer}=           Create Dictionary
    ...                 name=${nome_pagador}
    ...                 email=${email_pagador}
    
    # Criar o ticket
    &{ticket}=          Create Dictionary
    ...                 id=${ticket_type_id}
    ...                 holders=${holders_list}
    
    # Criar lista de tickets CORRETAMENTE
    @{tickets_list}=    Create List    ${ticket}
    
    # Criar payload final
    &{payload}=         Create Dictionary
    ...                 tickets=${tickets_list}
    ...                 payer=${payer}
    
    RETURN    ${payload}

Criar Ticket Type Com Idade Minima
    [Documentation]    Cria evento com ageRestriction e ticket type associado
    
    # Primeiro criar evento com ageRestriction
    ${event_name}=      Gerar Nome Evento Para Tickets
    &{event_payload}=   Create Dictionary
    ...                 name=${event_name}
    ...                 location=${SUCCESS_LOCATION}
    ...                 capacity=${SUCCESS_CAPACITY}
    ...                 ageRestriction=18    
    ...                 dateInitial=${FUTURE_DATE_INITIAL}
    ...                 dateFinal=${FUTURE_DATE_FINAL}
    
    ${headers}=         Create Dictionary    Content-Type=application/json
    ${response_event}=  POST On Session     api_session     ${ENDPOINT_EVENT}
    ...                 json=${event_payload}
    ...                 headers=${headers}
    ...                 expected_status=any
    
    Should Be Equal As Integers      ${response_event.status_code}     201
    ${event_id}=        Set Variable    ${response_event.json()['id']}
    
    # Agora criar ticket type para este evento
    ${ticket_name}=     Gerar Nome Ticket Aleatorio
    &{ticket_payload}=  Criar Payload Ticket Basico    ${ticket_name}    ${120}    ${10}
    ${response_ticket}=    Executar Post Ticket Para Evento    ${event_id}     ${ticket_payload}      ${201}
    ${ticket_type_id}=    Set Variable    ${response_ticket.json()['id']}
    RETURN    ${ticket_type_id}

Criar Payload Compra Ticket Com Menor Idade
    [Arguments]    ${ticket_type_id}
    [Documentation]    Cria payload com titular menor de idade CORRETAMENTE
    
    ${random_suffix}=    Generate Random String    10    [NUMBERS]
    ${nome_pagador}=     Set Variable    TESTUSER${random_suffix}
    ${email_pagador}=    Set Variable    test_user_${random_suffix}@test.com
    
    # Titular com 16 anos (menor que 18)
    ${data_nascimento_menor}=    Convert Date    2008-01-01    result_format=%Y-%m-%d
    
    # Criar holder
    &{holder}=          Create Dictionary
    ...                 name=Adolescente Teste
    ...                 email=adolescente@example.com
    ...                 birthDate=${data_nascimento_menor}
    
    # Criar lista de holders
    @{holders_list}=    Create List    ${holder}
    
    &{payer}=           Create Dictionary
    ...                 name=${nome_pagador}
    ...                 email=${email_pagador}
    
    # Criar ticket com lista correta
    &{ticket}=          Create Dictionary
    ...                 id=${ticket_type_id}
    ...                 holders=${holders_list}
    
    # Criar lista de tickets
    @{tickets_list}=    Create List    ${ticket}
    
    # Criar payload final
    &{payload}=         Create Dictionary
    ...                 tickets=${tickets_list}
    ...                 payer=${payer}
    
    RETURN    ${payload}

# --- KEYWORDS ADICIONAIS PARA CENÁRIOS ESPECÍFICOS ---

Criar_Ticket_Type_Com_Capacidade
    [Arguments]    ${capacidade}
    [Documentation]    Cria ticket type com capacidade específica
    ${event_id}=        Criar Evento Base Para Tickets
    ${ticket_name}=     Gerar Nome Ticket Aleatorio
    
    &{ticket_payload}=  Criar Payload Ticket Basico    ${ticket_name}    ${100}    ${capacidade}
    ${response}=        Executar Post Ticket Para Evento    ${event_id}    ${ticket_payload}    ${201}
    
    ${ticket_type_id}=    Set Variable    ${response.json()['id']}
    Should Not Be Empty    ${ticket_type_id}
    RETURN    ${ticket_type_id}

Criar_Ticket_Type_Com_Preco
    [Arguments]    ${preco}
    [Documentation]    Cria ticket type com preço específico
    ${event_id}=        Criar Evento Base Para Tickets
    ${ticket_name}=     Gerar Nome Ticket Aleatorio
    
    &{ticket_payload}=  Criar Payload Ticket Basico    ${ticket_name}    ${preco}    ${50}
    ${response}=        Executar Post Ticket Para Evento    ${event_id}    ${ticket_payload}    ${201}
    
    ${ticket_type_id}=    Set Variable    ${response.json()['id']}
    Should Not Be Empty    ${ticket_type_id}
    RETURN    ${ticket_type_id}

Executar Pagamento Mercado Pago Via API
    [Arguments]    ${ticket_type_id}    ${quantidade_tickets}=1
    [Documentation]    Fluxo COMPLETO integrado: evento → ticket type → pagamento
    
    Log    \n💰 INICIANDO PAGAMENTO VIA API MERCADO PAGO    console=True
    
    # === FASE 1: GERAR TOKEN DO CARTÃO ===
    Log    🔵 FASE 1: Gerando token do cartão    console=True
    ${card_token}=    Gerar Token Cartao Mercado Pago
    
    # === FASE 2: CRIAR PAYLOAD DE COMPRA COM TOKEN E TICKET_TYPE_ID ===
    Log    🔵 FASE 2: Criando payload de compra com token    console=True
    &{payload_compra}=    Criar Payload Compra Com Token    ${ticket_type_id}    ${quantidade_tickets}    ${card_token}
    
    # === FASE 3: EXECUTAR COMPRA COM TOKEN ===
    Log    🔵 FASE 3: Executando compra com token    console=True
    ${response_compra}=    Executar Compra Com Token    ${payload_compra}
    
    Log    ✅ PAGAMENTO VIA API CONCLUÍDO COM SUCESSO!    console=True
    RETURN    ${response_compra}

Criar Evento E Ticket Type Para Pagamento
    [Documentation]    Cria evento e ticket type específicos para testes de pagamento
    
    Log    \n🎪 CRIANDO EVENTO E TICKET TYPE PARA PAGAMENTO    console=True
    
    # === CRIAR EVENTO ===
    ${event_id}=    Criar Evento Base Para Tickets
    Log    Evento criado: ${event_id}    console=True
    
    # === CRIAR TICKET TYPE ===
    ${ticket_name}=    Gerar Nome Ticket Aleatorio
    &{ticket_payload}=    Criar Payload Ticket Basico    ${ticket_name}    150.00    5
    ${response_ticket}=    Executar Post Ticket Para Evento    ${event_id}    ${ticket_payload}    201
    ${ticket_type_id}=    Set Variable    ${response_ticket.json()['id']}
    
    Log    Ticket Type criado: ${ticket_type_id}    console=True
    Log    Preço: R$ 150.00 | Capacidade: 5 vagas    console=True
    
    RETURN    ${ticket_type_id}

Executar Fluxo Completo Pagamento
    [Arguments]    ${quantidade_tickets}=1
    [Documentation]    Fluxo COMPLETO ponta a ponta: evento → ticket → pagamento
    
    # === FASE 1: CRIAR INFRAESTRUTURA (EVENTO + TICKET) ===
    Log    \n🔵 FASE 1: Criando evento e ticket type    console=True
    ${ticket_type_id}=    Criar Evento E Ticket Type Para Pagamento
    
    # === FASE 2: EXECUTAR PAGAMENTO ===
    Log    \n💰 FASE 2: Executando pagamento via API Mercado Pago    console=True
    ${response_compra}=    Executar Pagamento Mercado Pago Via API    ${ticket_type_id}    ${quantidade_tickets}
    
    RETURN    ${response_compra}    ${ticket_type_id}

Gerar Token Cartao Mercado Pago
    [Documentation]    Gera token do cartão via API Mercado Pago usando cartões de teste oficiais
    
    # ✅ CORREÇÃO: Usar cartões de teste OFICIAIS do Mercado Pago
    # Cartões de teste: https://www.mercadopago.com.br/developers/pt/docs/your-integrations/test/cards
    &{identification}=    Create Dictionary
    ...    type=CPF
    ...    number=12345678909
    
    &{cardholder}=    Create Dictionary
    ...    name=APRO
    ...    identification=${identification}
    
    # ✅ CORREÇÃO: Cartão de teste OFICIAL (Visa)
    &{card_data}=    Create Dictionary
    ...    card_number=4235647728025682    # Cartão Visa de teste
    ...    security_code=123
    ...    expiration_month=11
    ...    expiration_year=2025
    ...    cardholder=${cardholder}
    
    ${public_key}=    Set Variable    ${public_key}
    ${url}=    Set Variable    https://api.mercadopago.com/v1/card_tokens?public_key=${public_key}
    
    ${headers}=    Create Dictionary
    ...    Content-Type=application/json
    
    Log    Enviando requisição para gerar token...    console=True
    ${response}=    POST    ${url}
    ...    json=${card_data}
    ...    headers=${headers}
    ...    expected_status=201
    
    # Validar resposta
    Should Be Equal As Integers    ${response.status_code}    201
    ${response_json}=    Set Variable    ${response.json()}
    
    # Extrair e validar token
    ${card_token}=    Set Variable    ${response_json['id']}
    Should Not Be Empty    ${card_token}
    
    Log    ✅ Token gerado com cartão de teste: ${card_token}    console=True
    RETURN    ${card_token}

Criar Payload Compra Com Token
    [Arguments]    ${ticket_type_id}    ${quantidade_tickets}    ${card_token}
    [Documentation]    Cria payload de compra incluindo o token do cartão
    
    ${random_suffix}=    Generate Random String    10    [NUMBERS]
    ${nome_pagador}=     Set Variable    TESTUSER${random_suffix}
    ${email_pagador}=    Set Variable    test_user_${random_suffix}@test.com
    
    # Criar holders
    @{holders_list}=    Create List
    FOR    ${index}    IN RANGE    ${quantidade_tickets}
        ${holder_name}=     FakerLibrary.Name
        ${holder_email}=    FakerLibrary.Email
        ${anos_aleatorios}=    Evaluate    random.randint(18, 65)
        ${data_atual}=      Get Current Date
        ${data_nascimento}=    Subtract Time From Date    ${data_atual}    ${anos_aleatorios * 365} days
        ${holder_birth_date}=    Convert Date    ${data_nascimento}    result_format=%Y-%m-%d
        
        &{holder}=    Create Dictionary
        ...    name=${holder_name}
        ...    email=${holder_email}
        ...    birthDate=${holder_birth_date}
        
        Append To List    ${holders_list}    ${holder}
    END
    
    # Criar payer
    &{payer}=    Create Dictionary
    ...    name=${nome_pagador}
    ...    email=${email_pagador}
    
    # Criar ticket
    &{ticket}=    Create Dictionary
    ...    id=${ticket_type_id}
    ...    holders=${holders_list}
    
    # Criar payload FINAL com token
    @{tickets_list}=    Create List    ${ticket}
    
    &{payload}=    Create Dictionary
    ...    tickets=${tickets_list}
    ...    payer=${payer}
    ...    token=${card_token}
    
    Log    Payload com token criado    console=True
    RETURN    ${payload}

Executar Compra Com Token
    [Arguments]    ${payload}
    [Documentation]    Executa compra incluindo token do cartão
    
    ${headers}=    Create Dictionary
    ...    Content-Type=application/json
    
    # Log detalhado do payload ANTES de enviar
    Log    \n🔍 DEBUG - PAYLOAD QUE SERÁ ENVIADO:    console=True
    Log    Tickets: ${payload['tickets']}    console=True
    Log    Payer: ${payload['payer']}    console=True  
    Log    Token: ${payload['token']}    console=True
    
    ${response}=    POST On Session    api_session    ${ENDPOINT_BUY_TICKET}
    ...    json=${payload}
    ...    headers=${headers}
    ...    expected_status=any  # ← Aceitar qualquer status para ver o erro real
    
    # Log detalhado da resposta
    Log    \n🔍 DEBUG - RESPOSTA DA API:    console=True
    Log    Status Code: ${response.status_code}    console=True
    Log    Response Body: ${response.text}    console=True
    Log    Response Headers: ${response.headers}    console=True
    
    # Se for 422, falhar com mensagem clara
    Run Keyword If    ${response.status_code} == 422
    ...    Fail    ❌ ERRO 422 - VALIDAÇÃO: ${response.text}
    
    # Se for outro erro inesperado
    Run Keyword If    ${response.status_code} != 200
    ...    Fail    ❌ ERRO ${response.status_code}: ${response.text}
    
    # Se chegou aqui, é 200 - validar sucesso
    Should Be Equal As Integers    ${response.status_code}    200
    
    RETURN    ${response}


Criar_Ticket_Type_E_Capturar_ID
    [Documentation]    Cria um ticket type e retorna o ID
    ${event_id}=        Criar Evento Base Para Tickets
    ${ticket_name}=     Gerar Nome Ticket Aleatorio
    
    &{ticket_payload}=  Criar Payload Ticket Basico    ${ticket_name}    ${100}    ${50}
    ${response}=        Executar Post Ticket Para Evento    ${event_id}    ${ticket_payload}    ${201}
    
    ${ticket_type_id}=    Set Variable    ${response.json()['id']}
    Should Not Be Empty    ${ticket_type_id}
    RETURN    ${ticket_type_id}

Executar_Compra_Ticket
    [Arguments]    ${ticket_type_id}    ${quantidade_tickets}=1    ${expected_status}=200
    [Documentation]    Executa a compra de ticket com payload válido incluindo token
    
    # === GERAR TOKEN DO CARTÃO ===
    ${card_token}=    Gerar Token Cartao Mercado Pago
    
    # === CRIAR PAYLOAD VÁLIDO ===
    ${random_suffix}=    Generate Random String    10    [NUMBERS]
    ${nome_pagador}=     Set Variable    TESTUSER${random_suffix}
    ${email_pagador}=    Set Variable    test_user_${random_suffix}@test.com
    
    # Criar holders
    @{holders_list}=    Create List
    FOR    ${index}    IN RANGE    ${quantidade_tickets}
        ${holder_name}=     FakerLibrary.Name
        ${holder_email}=    FakerLibrary.Email
        ${anos_aleatorios}=    Evaluate    random.randint(18, 65)
        ${data_atual}=      Get Current Date
        ${data_nascimento}=    Subtract Time From Date    ${data_atual}    ${anos_aleatorios * 365} days
        ${holder_birth_date}=    Convert Date    ${data_nascimento}    result_format=%Y-%m-%d
        
        &{holder}=    Create Dictionary
        ...    name=${holder_name}
        ...    email=${holder_email}
        ...    birthDate=${holder_birth_date}
        
        Append To List    ${holders_list}    ${holder}
    END
    
    # Criar payer
    &{payer}=    Create Dictionary
    ...    name=${nome_pagador}
    ...    email=${email_pagador}
    
    # Criar ticket
    &{ticket}=    Create Dictionary
    ...    id=${ticket_type_id}
    ...    holders=${holders_list}
    
    # Criar payload FINAL com token
    @{tickets_list}=    Create List    ${ticket}
    
    &{payload}=    Create Dictionary
    ...    tickets=${tickets_list}
    ...    payer=${payer}
    ...    token=${card_token}
    
    # === EXECUTAR REQUISIÇÃO ===
    ${headers}=     Create Dictionary   Content-Type=application/json
    ${response}=    POST On Session     api_session     ${ENDPOINT_BUY_TICKET}
    ...             json=${payload} 
    ...             headers=${headers}
    ...             expected_status=any
    
    Should Be Equal As Integers    ${response.status_code}    ${expected_status}
    RETURN    ${response}

Executar_Compra_Ticket_Com_Payload
    [Arguments]    ${payload}    ${expected_status}
    [Documentation]    Executa compra com payload customizado (para cenários de falha)
    
    # Verificar se o payload já tem token, se não, adicionar
    ${has_token}=    Run Keyword And Return Status    Dictionary Should Contain Key    ${payload}    token
    Run Keyword Unless    ${has_token}
    ...    Add Token To Payload    ${payload}
    
    ${headers}=     Create Dictionary   Content-Type=application/json
    ${response}=    POST On Session     api_session     ${ENDPOINT_BUY_TICKET}
    ...             json=${payload} 
    ...             headers=${headers}
    ...             expected_status=any
    
    Should Be Equal As Integers    ${response.status_code}    ${expected_status}
    RETURN    ${response}

Add Token To Payload
    [Arguments]    ${payload}
    [Documentation]    Adiciona token do cartão ao payload
    
    ${card_token}=    Gerar Token Cartao Mercado Pago
    Set To Dictionary    ${payload}    token=${card_token}

Validar_Response_Compra_Sucesso
    [Arguments]     ${response}
    [Documentation]    Valida resposta de sucesso da compra
    
    ${response_json}=   Set Variable    ${response.json()}
    Should Not Be Empty     ${response_json['orderId']}
    Should Not Be Empty     ${response_json['status']}  
    

# --- KEYWORDS ESPECÍFICAS PARA PAGAMENTO ---

Criar Payload Compra Ticket Com Valor Especifico
    [Arguments]    ${ticket_type_id}    ${valor}=100.00
    [Documentation]    Cria payload com valor específico para testes de pagamento
    
    ${random_suffix}=    Generate Random String    10    [NUMBERS]
    ${nome_pagador}=     Set Variable    TESTUSER${random_suffix}
    ${email_pagador}=    Set Variable    test_user_${random_suffix}@test.com
    
    # Gerar dados do holder
    ${holder_name}=     FakerLibrary.Name
    ${holder_email}=    FakerLibrary.Email
    ${anos_aleatorios}=    Evaluate    random.randint(18, 65)
    ${data_atual}=      Get Current Date
    ${data_nascimento}=    Subtract Time From Date    ${data_atual}    ${anos_aleatorios * 365} days
    ${holder_birth_date}=    Convert Date    ${data_nascimento}    result_format=%Y-%m-%d
    
    &{holder}=          Create Dictionary
    ...                 name=${holder_name}
    ...                 email=${holder_email}
    ...                 birthDate=${holder_birth_date}
    
    &{payer}=           Create Dictionary
    ...                 name=${nome_pagador}
    ...                 email=${email_pagador}
    
    &{ticket}=    Create Dictionary
...           id=${ticket_type_id}
...           holders=[${holder}] 
    
    &{payload}=    Create Dictionary
...            tickets=[${ticket}]
...            payer=${payer}
    
    RETURN    ${payload}

Executar Patch Ticket Type
    [Arguments]    ${ticket_type_id}    ${payload}    ${expected_status}
    [Documentation]    Executa PATCH para atualizar ticket type
    
    ${url_final}=    Replace String    ${ENDPOINT_UPDATE_TICKET}    {ticketTypeId}    ${ticket_type_id}
    ${headers}=     Create Dictionary   Content-Type=application/json
    
    ${response}=    PATCH On Session    api_session    ${url_final}
    ...             json=${payload} 
    ...             headers=${headers}
    ...             expected_status=any
    
    Should Be Equal As Integers    ${response.status_code}    ${expected_status}
    RETURN    ${response}

Validar Response Ticket Atualizado
    [Arguments]    ${response}    ${ticket_type_id}    ${event_id}
    [Documentation]    Valida resposta de sucesso da atualização
    
    ${response_json}=    Set Variable    ${response.json()}
    
    # Validações básicas
    Should Be Equal    ${response_json['id']}    ${ticket_type_id}
    Should Be Equal    ${response_json['event']}    ${event_id}
    Should Not Be Empty    ${response_json['updatedAt']}

Criar Payload Atualizacao Parcial
    [Arguments]    ${campo}    ${valor}
    [Documentation]    Cria payload para atualização parcial
    
    &{payload}=    Create Dictionary    ${campo}=${valor}
    RETURN    ${payload}

# --- KEYWORDS PARA CENÁRIOS ESPECÍFICOS PONTA A PONTA ---

Criar Evento E Ticket Para Atualizacao
    [Documentation]    Fluxo completo: Cria evento e ticket type para testes PATCH
    
    # 1. Criar Evento (usando keyword existente)
    ${event_id}=    Criar Evento Base Para Tickets
    
    # 2. Criar Ticket Type (usando keyword existente)
    ${ticket_name}=    Gerar Nome Ticket Aleatorio
    &{ticket_payload}=    Criar Payload Ticket Basico    ${ticket_name}    ${100}    ${50}
    ${response_ticket}=    Executar Post Ticket Para Evento    ${event_id}    ${ticket_payload}    ${201}
    ${ticket_type_id}=    Set Variable    ${response_ticket.json()['id']}
    
    RETURN    ${event_id}    ${ticket_type_id}

Criar Fluxo Comprar E Atualizar
    [Arguments]    ${capacidade_ticket}=60    ${quantidade_compras}=5
    [Documentation]    Fluxo completo: Evento → Ticket → Compras → Tentar Atualizar
    
    # 1. Criar Evento
    ${event_id}=    Criar Evento Base Para Tickets
    
    # 2. Criar Ticket Type com capacidade específica
    ${ticket_name}=    Gerar Nome Ticket Aleatorio
    &{ticket_payload}=    Criar Payload Ticket Basico    ${ticket_name}    ${100}    ${capacidade_ticket}
    ${response_ticket}=    Executar Post Ticket Para Evento    ${event_id}    ${ticket_payload}    ${201}
    ${ticket_type_id}=    Set Variable    ${response_ticket.json()['id']}
    
    # 3. Realizar compras (se solicitado)
    IF    ${quantidade_compras} > 0
        ${payload_compra}=    Criar Payload Compra Ticket    ${ticket_type_id}
        ${response_compra}=    Executar_Compra_Ticket    ${payload_compra}    ${201}
    END
    
    RETURN    ${event_id}    ${ticket_type_id}

Criar Dois Tickets No Mesmo Evento
    [Documentation]    Cria dois tickets no mesmo evento para teste de nome duplicado
    
    # 1. Criar Evento
    ${event_id}=    Criar Evento Base Para Tickets
    
    # 2. Criar Primeiro Ticket
    ${ticket_name_1}=    Gerar Nome Ticket Aleatorio
    &{ticket_payload_1}=    Criar Payload Ticket Basico    ${ticket_name_1}    ${100}    ${30}
    ${response_ticket_1}=    Executar Post Ticket Para Evento    ${event_id}    ${ticket_payload_1}    ${201}
    ${ticket_type_id_1}=    Set Variable    ${response_ticket_1.json()['id']}
    
    # 3. Criar Segundo Ticket
    ${ticket_name_2}=    Gerar Nome Ticket Aleatorio
    &{ticket_payload_2}=    Criar Payload Ticket Basico    ${ticket_name_2}    ${150}    ${20}
    ${response_ticket_2}=    Executar Post Ticket Para Evento    ${event_id}    ${ticket_payload_2}    ${201}
    ${ticket_type_id_2}=    Set Variable    ${response_ticket_2.json()['id']}
    
    RETURN    ${event_id}    ${ticket_type_id_1}    ${ticket_type_id_2}    ${ticket_name_1}

Gerar UUID Inexistente
    [Documentation]    Gera um UUID no formato correto que não existe na base
    # Formato: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
    ${part1}=    Generate Random String    8    [LETTERS][NUMBERS]
    ${part2}=    Generate Random String    4    [LETTERS][NUMBERS]
    ${part3}=    Generate Random String    4    [LETTERS][NUMBERS]
    ${part4}=    Generate Random String    4    [LETTERS][NUMBERS]
    ${part5}=    Generate Random String    12    [LETTERS][NUMBERS]
    
    ${uuid_inexistente}=    Set Variable    ${part1}-${part2}-${part3}-${part4}-${part5}
    RETURN    ${uuid_inexistente}

Executar Delete Ticket Type
    [Arguments]    ${ticket_type_id}    ${expected_status}
    [Documentation]    Executa DELETE para remover ticket type
    
    ${url_final}=    Replace String    ${ENDPOINT_DELETE_TICKET}    {ticketTypeId}    ${ticket_type_id}
    
    ${response}=    DELETE On Session    api_session    ${url_final}
    ...             expected_status=${expected_status}
    
    RETURN    ${response}